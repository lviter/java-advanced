package com.llh.advance.collection;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author Administrator
 */
public class SetTest {

    /**
     * 并发修改异常，线程不安全
     *
     * @param args
     */
    public static void main(String[] args) {
        //Hashset底层是HashMap
        Set<String> set = new HashSet<>();

        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(set);
            }, String.valueOf(i)).start();
        }
    }
}

/**
 * 解决并发修改问题
 */
class CopyOnWriteArraySet {
    public static void main(String[] args) {
        //底层使用的CopyOnWriteArrayList
        Set<String> set = new java.util.concurrent.CopyOnWriteArraySet<>();

        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(set);
            }, String.valueOf(i)).start();

        }
    }
}
