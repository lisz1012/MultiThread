package com.lisz.container;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CountDownLatch;

public class T01_ConcurrentHashMap {
    private static Random r = new Random();
    private static Thread ths[] = new Thread[100];

    public static void main(String[] args) {
        Map<String, String> map = new ConcurrentHashMap<>(); //不存在ConcurrentTreeMap，原因是CAS用在Tree的节点上会非常复杂
        run(map);
        map = new ConcurrentSkipListMap<>(); // 高并发且排序，查找元素的时候从上层最少元素的那一层开始往下查找
        run(map);
        map = new Hashtable<>();
        run(map);
        map = new HashMap<>();
        run(map);
    }

    private static void run (Map<String, String> map) {

        CountDownLatch latch = new CountDownLatch(ths.length);
        long start = System.currentTimeMillis();
        for (int i = 0; i < ths.length; i++) {
            ths[i] = new Thread(()->{
                for (int j = 0; j < 10000; j++) {
                    map.put("a" + r.nextInt(10000), "a" + r.nextInt(10000));
                }
                latch.countDown();
            });
        }
        Arrays.asList(ths).forEach((t)->t.start());
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(map.getClass().getName() + ": " + (System.currentTimeMillis() - start) + "ms");
    }
}
