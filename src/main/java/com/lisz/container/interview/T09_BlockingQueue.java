package com.lisz.container.interview;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class T09_BlockingQueue {
    private AtomicInteger threadNo = new AtomicInteger(1);
    private char aC[] = "ABCDEFG".toCharArray();
    private char aI[] = "1234567".toCharArray();
    private Thread t1 = null;
    private Thread t2 = null;
    private BlockingQueue<String> q1 = new ArrayBlockingQueue(1);
    private BlockingQueue<String> q2 = new ArrayBlockingQueue(1);

    @Test
    public void test() {
        t1 = new Thread(()->{
           for (char c : aC) {
               System.out.print(c);
               try {
                   q1.put("ok");
                   q2.take();
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
        });
        t2 = new Thread(()->{
           for (char c : aI) {
               try {
                   q1.take();
                   System.out.print(c);
                   q2.put("ok");
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
        });
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
