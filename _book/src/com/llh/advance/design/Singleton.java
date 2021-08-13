package com.llh.advance.design;

/**
 * @author lviter
 * 单例模式的四种实现：
 * 1. 饿汉
 * 2. 懒汉
 * 3. DCL双端检锁机制
 * 4. 静态内部类
 * 5. 枚举模式
 */
public class Singleton {
    public static void main(String[] args) {
        SingletonEnum s1 = SingletonEnum.INSTANCE.getInstance();
        SingletonEnum s2 = SingletonEnum.INSTANCE.getInstance();
        System.out.println(s1 == s2);
    }
}

/**
 * 懒汉式--线程不安全
 */
class SingletonLazy {
    private static SingletonLazy singletonLazy = null;

    private SingletonLazy() {
        System.out.println("懒汉式---线程不安全的构造方法");
    }

    public static SingletonLazy getSingletonLazy() {
        if (singletonLazy == null) {
            return new SingletonLazy();
        }
        return singletonLazy;
    }
}

/**
 * 饿汉式
 * 没有加锁，执行效率会提高。缺点是类加载时就初始化，浪费内存
 * 基于classloder机制避免了多线程的同步问题
 */
class SingletonHungry {
    private static final SingletonHungry singletonHungry = new SingletonHungry();

    private SingletonHungry() {
        System.out.println("饿汉式");
    }

    public static SingletonHungry getSingletonHungry() {
        return singletonHungry;
    }
}

/**
 * DCL:double check lock,双端检锁机制--在同步锁前后都增加检查操作
 * 多线程安全，采用双锁机制，安全且在多线程下保持高性能。
 */
class SingletonDcl {

    private volatile static SingletonDcl singletonDcl;

    private SingletonDcl() {
        System.out.println("DCL双端检锁--线程安全，支持高性能");
    }

    /**
     * 同步锁前后都增加检查
     *
     * @return
     */
    public static SingletonDcl getSingletonDcl() {
        if (singletonDcl == null) {
            synchronized (SingletonDcl.class) {
                if (singletonDcl == null) {
                    singletonDcl = new SingletonDcl();
                }
            }
        }
        return singletonDcl;
    }
}

/**
 * 静态内部类的构造方法只会在调用他的时候触发，所以是线程安全的
 */
class SingletonStaticInternal {

    private SingletonStaticInternal() {
        System.out.println("这里是静态内部类的方式");
    }

    private static class holder {
        private static final SingletonStaticInternal instance = new SingletonStaticInternal();
    }

    public static SingletonStaticInternal getInstance() {
        return holder.instance;
    }
}

/**
 * 枚举单例(单例模式的最佳实现方法)
 * 既可以避免多线程同步问题；还可以防止通过反射和反序列化来重新创建新的对象
 */
enum SingletonEnum {
    /**
     * 单例
     */
    INSTANCE;

    public SingletonEnum getInstance() {
        return INSTANCE;
    }

    public void m() {
        System.out.println("枚举类");
    }
}
