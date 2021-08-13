package com.llh.advance.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author lviter
 */
public class CopyOnWriteArrayListTest {
    public static void main(String[] args) {

        CopyOnWriteArrayList<String> copyOnWriteArrayList = new CopyOnWriteArrayList();
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                copyOnWriteArrayList.add(UUID.randomUUID().toString().substring(0, 8));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(copyOnWriteArrayList);
            }, "t1").start();
        }
    }
}

class ArrayListDemo {

    public static List<String> addArrayList() {
        List<String> strings = new ArrayList<>();
        strings.add(UUID.randomUUID().toString().substring(0, 8));
        return strings;
    }

}

class ArrayListSync {
    public static List<String> addArrayList() {
        List<String> strings = Collections.synchronizedList(new ArrayList<>());
        strings.add(UUID.randomUUID().toString().substring(0, 8));
        return strings;
    }
}