package com.llh.advance.concurrent.thread;


import java.util.Arrays;
import java.util.Random;

/**
 * @author: lviter
 * @desc:
 * @date:2021/11/11
 */
public class RedBluePredict {

    public static void main(String[] args) {
        //1-33红号，1-16蓝号
        Integer[] red = new Integer[6];
        Integer blue =new Random().nextInt(16);

        for (int i = 0; i < 6; i++) {
            red[i] = new Random().nextInt(33);
        }
        System.out.println("red:");
        for (Integer integer : red) {
            System.out.println(integer);
        }
        System.out.println("blue:"+blue);
    }
}
