## Redis实现延迟队列
1. 失效监听
2. redisson实现发布订阅延迟
### redis失效监听事件
集成KeyExpirationEventMessageListener类实现redis失效监听事件
#### 此种实现面临的问题
1. redis的失效监听事件会存在一定的时间差，并且当数据量越大时，误差会越大。
2. redis的失效监听事件会将所有key失效都会通知到onMessage,如果针对一个key，分布式业务的场景下，会出现重复消费的问题。（可以增加分布式锁的实现，但是redisson分布式锁提供了另一种延迟队列的实现方式）
#### 开发准备
redis需要在服务端开启配置，打开redis服务的配置文件   添加`notify-keyspace-events Ex`
- 相关参数如下：
```yaml
K：keyspace事件，事件以__keyspace@<db>__为前缀进行发布；        
E：keyevent事件，事件以__keyevent@<db>__为前缀进行发布；        
g：一般性的，非特定类型的命令，比如del，expire，rename等；       
$：字符串特定命令；        
l：列表特定命令；        
s：集合特定命令；        
h：哈希特定命令；        
z：有序集合特定命令；        
x：过期事件，当某个键过期并删除时会产生该事件；        
e：驱逐事件，当某个键因maxmemore策略而被删除时，产生该事件；        
A：g$lshzxe的别名，因此”AKE”意味着所有事件。
```
#### 基础实现
1. 加入依赖
```yaml
<dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```
2. 可正常连接存取redis数据之后，创建监听类`RedisKeyExpirationListener`继承`KeyExpirationEventMessageListener`，重写`onMessage`方法。（key失效之后，会发出onMessage方法，之呢个获取失效的key值，不能获取key对应的value值）。
```java
import com.dadi01.scrm.service.member.api.common.MemberStatusEnum;
import com.dadi01.scrm.service.member.provider.service.base.IBaseMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * @author lviter
 */
@Component
@Slf4j
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
    private final IBaseMemberService baseMemberService;


    private final static String MEMBER_LOCK_ACCOUNT_SUFFIX = ".lock_account";
    private final static String MEMBER_LOCK_ACCOUNT_DOMAIN_SUFFIX = "T";
    private final static String MEMBER_LOCK_ACCOUNT_MEMBER_SUFFIX = "M";
    private final static String MEMBER_REDISSON_LOCK = ".member_lock_redisson";
    private final static int WAIT_TIME = 5;
    private final static int LEASE_TIME = 10;

    public RedisKeyExpirationListener(RedisMessageListenerContainer redisMessageListenerContainer, IBaseMemberService baseMemberService) {
        super(redisMessageListenerContainer);
        this.baseMemberService = baseMemberService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        //获取失效的key
        String expiredKey = message.toString();
        log.info("================================get on message:{}====================", expiredKey);
        if (expiredKey.endsWith(MEMBER_LOCK_ACCOUNT_SUFFIX)) {
            log.info("================================on message:{}====================", expiredKey);
            try {
                log.info("=======待解锁账号解锁======expiredKey:{}", expiredKey);
                String tenantId = expiredKey.substring(expiredKey.indexOf(MEMBER_LOCK_ACCOUNT_DOMAIN_SUFFIX) + 1, expiredKey.indexOf(MEMBER_LOCK_ACCOUNT_MEMBER_SUFFIX));
                String memberId = expiredKey.substring(expiredKey.indexOf(MEMBER_LOCK_ACCOUNT_MEMBER_SUFFIX) + 1, expiredKey.indexOf(MEMBER_LOCK_ACCOUNT_SUFFIX));
                baseMemberService.updateAccount(Integer.parseInt(tenantId), Long.parseLong(memberId), MemberStatusEnum.NORMAL.getCode(), null);
            } catch (Exception exception) {
                log.info("auto unlock fail,expired key:{},exception:{}", expiredKey, exception.getMessage());
            }
        }
    }
}

```
3. 创建一个配置类`RedisConfig`
```java
/**
 * @author lviter
 */
@Configuration
public class RedisConfig {

    @Value("${redis.dbIndex}")
    private Integer dbIndex;

    private final String TOPIC = "__keyevent@" + dbIndex + "__:expired";
    private final RedisConnectionFactory redisConnectionFactory;

    public RedisConfig(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }


    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
        //keyevent事件，事件以__keyevent@<db>__为前缀进行发布
        //db为redis第几个库 db2...
//        redisMessageListenerContainer.addMessageListener(redisKeyExpirationListener, new PatternTopic(TOPIC));
        return redisMessageListenerContainer;
    }
}

```