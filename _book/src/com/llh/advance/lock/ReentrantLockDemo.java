package com.llh.advance.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Administrator
 * t1,t2证明了synchronized是一个可重入锁
 * t3,t4证明了ReentrantLock是一个可重入锁
 */
public class ReentrantLockDemo {
    public static void main(String[] args) {
        Phone phone = new Phone();
//        new Thread(() -> phone.sendSms(), "t1").start();

//        new Thread(() -> phone.sendSms(), "t2").start();

        Thread t3 = new Thread(phone);
        Thread t4 = new Thread(phone);
        t3.start();
        t4.start();
    }
}

class Phone implements Runnable {

    /**
     * 在同一个线程在外层方法获取锁的时候，在进入内层方法会自动获取锁
     */
    public synchronized void sendSms() {
        System.out.println(Thread.currentThread().getName() + "invoked sendSms");
        //进入内层第二个synchronized修饰的方法
        sendEmail();
    }


    public synchronized void sendEmail() {
        System.out.println(Thread.currentThread().getName() + "invoked sendEmail");
    }

    Lock lock = new ReentrantLock();

    @Override
    public void run() {
        get();
    }

    public void get() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "invoked get");
            set();
        } finally {
            lock.unlock();
        }
    }

    public void set() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "invoked set+++++");
        } finally {
            lock.unlock();
        }
    }
}