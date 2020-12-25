### OutOfMemoryError异常
- 注：本人测试基于jdk1.8测试，有部分不同但是原理可以了解，感兴趣可以下载jdk1.7配套测试
- Java虚拟机规范中描述，除了程序计数器，虚拟机的其他几个运行时区域都有发生OOM异常的可能。
下面的示例代码都基于HotSpot虚拟机运行，设置VM参数可以在IDE的VM options内设置,如图
![](https://llhyoudao.oss-cn-shenzhen.aliyuncs.com/%E6%9C%89%E9%81%93%E4%BA%91/103.jpg)

#### Java堆溢出
引发思路：Java堆用于存储对象实例，只要不断地创建对象，并且保证GC Roots到对象之间有可达路径来避免垃圾回收机制清除这些对象，那么在对象数量到达最大堆的容量限制后就会产生内存溢出异常。
- 以下代码需要配置VM，设置java堆大小20MB,不可扩展（将堆的最小值-Xms参数与最大值-Xmx参数设置为一样即可避免堆自动扩展），-XX：+HeapDumpOnOutOfMemoryError可以让虚拟机在出现内存溢出异常时Dump出当前的内存堆转储快照以便事后进行分析
-Xmx：最大堆大小
```yaml
-Xmx20M -Xms20M -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=E:\jvmlog\oom.hprof
```
可以指定hprof文件存放位置，之后使用jdk自带工具jvisualvm.exe打开分析即可

```java
import java.util.ArrayList;
import java.util.List;

public class HeapOOM {
    static class OOMObject {
    }
    public static void main(
            String[] args) {
        List<OOMObject> list = new ArrayList<OOMObject>();
        while (true) {
            list.add(new OOMObject());
        }
    }
}
```
运行结果：
```log
java.lang.OutOfMemoryError: Java heap space
Dumping heap to java_pid12092.hprof ...
Heap dump file created [28256955 bytes in 0.096 secs]
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
	at java.util.Arrays.copyOf(Arrays.java:3210)
	at java.util.Arrays.copyOf(Arrays.java:3181)
	at java.util.ArrayList.grow(ArrayList.java:267)
	at java.util.ArrayList.ensureExplicitCapacity(ArrayList.java:241)
	at java.util.ArrayList.ensureCapacityInternal(ArrayList.java:233)
	at java.util.ArrayList.add(ArrayList.java:464)
	at com.llh.jdk.map.HeapOOM.main(HeapOOM.java:14)
```
Java堆内存的OOM异常是实际应用中常见的内存溢出异常情况。当出现Java堆内存溢出时，异常堆栈信息“java.lang.OutOfMemoryError”会跟着进一步提示“Java heap space”。

![](https://llhyoudao.oss-cn-shenzhen.aliyuncs.com/%E6%9C%89%E9%81%93%E4%BA%91/104.jpg)
- 如果是内存泄露，可进一步通过工具查看泄露对象到GC Roots的引用链。于是就能找到泄露对象是通过怎样的路径与GC Roots相关联并导致垃圾收集器无法自动回收它们的。掌握了泄露对象的类型信息及GC Roots引用链的信息，就可以比较准确地定位出泄露代码的位置。
- 如果不存在泄露，换句话说，就是内存中的对象确实都还必须存活着，那就应当检查虚拟机的堆参数（-Xmx与-Xms），与机器物理内存对比看是否还可以调大，从代码上检查是否存在某些对象生命周期过长、持有状态时间过长的情况，尝试减少程序运行期的内存消耗。

#### 虚拟机栈和本地方法栈溢出
1. 在java虚拟机栈中描述了两种异常：
    - 如果线程请求的栈深度大于虚拟机所允许的最大深度，将抛出StackOverflowError异常
    - 如果虚拟机在扩展栈时无法申请到足够的内存空间，则抛出OutOfMemoryError异常
2. 定义大量的本地变量，增大此方法栈中本地变量表的长度，设置-Xss参数减少栈内存容量
```yaml
-Xss20M -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=E:\jvmlog\sof.hprof
```
```java
public class JavaVMStackSOF {
    private int stackLength = 1;

    public void stackLeak() {
        stackLength++;
        stackLeak();
    }
    public static void main(String[] args) throws Throwable {
        JavaVMStackSOF oom = new JavaVMStackSOF();
        try {
            oom.stackLeak();
        } catch (Throwable e) {
            System.out.println("stack length：" + oom.stackLength);
            throw e;
        }
    }
}
```
运行结果
```
stack length：1271382
Exception in thread "main" java.lang.StackOverflowError
	at com.llh.jdk.map.JavaVMStackSOF.stackLeak(JavaVMStackSOF.java:8)
	at com.llh.jdk.map.JavaVMStackSOF.stackLeak(JavaVMStackSOF.java:8)
	at com.llh.jdk.map.JavaVMStackSOF.stackLeak(JavaVMStackSOF.java:8)
	at com.llh.jdk.map.JavaVMStackSOF.stackLeak(JavaVMStackSOF.java:8)
	at com.llh.jdk.map.JavaVMStackSOF.stackLeak(JavaVMStackSOF.java:8)
	at com.llh.jdk.map.JavaVMStackSOF.stackLeak(JavaVMStackSOF.java:8)
	at com.llh.jdk.map.JavaVMStackSOF.stackLeak(JavaVMStackSOF.java:8)
	at com.llh.jdk.map.JavaVMStackSOF.stackLeak(JavaVMStackSOF.java:8)
···
```
实验结果：
单线程下，无论是由于栈帧太大还是虚拟机栈容量太小，当内存无法分配的时候，虚拟机抛出的都是StackOverflowError异常。


