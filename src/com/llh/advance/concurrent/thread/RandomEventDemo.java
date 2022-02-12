package com.llh.advance.concurrent.thread;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RandomEventDemo {

    public static void main(String[] args) {
       List<String> list = new ArrayList<>();
        list.add("12");
        Set<String> ss = new HashSet<>(list);
        ss.remove("12");
        System.out.println(ss);
    }
}
