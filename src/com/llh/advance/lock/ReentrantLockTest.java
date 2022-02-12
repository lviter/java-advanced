package com.llh.advance.lock;

import java.util.concurrent.TimeUnit;

public class ReentrantLockTest {
    public static void main(String[] args) {
        ReentrantLockTest reentrantLockTest = new ReentrantLockTest();
        new Thread(() -> {
            reentrantLockTest.b();
        }, "b1").start();

        Thread thread =new Thread(() -> {
            reentrantLockTest.a();
        }, "a1");
        thread.start();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(thread.getState());


    }

    synchronized void a() {
        System.out.println(Thread.currentThread().getName() + "进入A方法");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        b();
    }

    synchronized void b() {
        System.out.println(Thread.currentThread().getName() + "进入B方法");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "退出b方法");
    }


}
