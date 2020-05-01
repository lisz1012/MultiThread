package com.lisz.container;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

// put，慢了就阻塞；take，空了就等待。BlockingQueue天生就是多对线程友好的生产者消费者模型
// 阻塞的原理：LockSupport的park方法，而park又调用了Unsafe的public native void park(boolean isAbsolute, long time);
public class T05_LinkedBlockingQueue {
    private static LinkedBlockingQueue<String> strs = new LinkedBlockingQueue<>();
    private static Random r = new Random();

    public static void main(String[] args) {
        new Thread(()->{
            for (int i = 0; i < 100; i++) {
                try {
                    strs.put("a" + r.nextInt(1000));
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        for (int i = 0; i < 5; i++) {
            new Thread(()->{
                while (true) {
                    try {
                        System.out.println(Thread.currentThread().getName() + ": " + strs.take());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "c" + i).start();
        }
    }
}
