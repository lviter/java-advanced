package com.llh.advance.concurrent.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author: lviter
 * @desc:
 * @date:2021/11/1
 */
public class FutureThreadDemo {

    private static ThreadPoolExecutor timeThreadPoolExecutor = new ThreadPoolExecutor(5, 5 + 1,
            1L, TimeUnit.MINUTES, new LinkedBlockingQueue<>(10)
            , new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String[] args) {
        //一个策略器下所有批次号
        List<String> configs = new ArrayList<>();
        configs.add("123");
        configs.add("456");
        configs.add("789");
        configs.add("654");
        //开启多线程
        ExecutorService exs = Executors.newFixedThreadPool(configs.size());
        //使用阻塞队列，优先获取到结果的优先执行
        List<String> ress = new ArrayList<>();
        BlockingQueue<Future<String>> blockingQueue = new LinkedBlockingQueue<>();
        try {
            for (String config : configs) {
                blockingQueue.put(exs.submit(() -> getRes(config)));
            }
            //高速获取结果集
            while (blockingQueue.size() > 0) {
                Iterator<Future<String>> iterator = blockingQueue.iterator();
                while (iterator.hasNext()) {
                    Future<String> future = iterator.next();
                    if (future.isDone() && !future.isCancelled()) {
                        //获取结果
                        String i = future.get();
                        System.out.println("任务i=" + i + "获取完成，移出任务队列！" + new Date());
                        ress.add(i);
                        //任务完成移除任务
                        iterator.remove();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("exception");
        } finally {
            exs.shutdown();
        }

    }

    private static String getRes(String config) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("获取结果中======" + Thread.currentThread().getName() + "线程=====");
        return config;
    }

}