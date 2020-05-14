package com.lisz.container.interview;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class T07_AtomicInteger {
    private AtomicInteger threadNo = new AtomicInteger(1);
    private char aC[] = "ABCDEFG".toCharArray();
    private char aI[] = "1234567".toCharArray();
    private Thread t1 = null;
    private Thread t2 = null;

    @Test
    public void test() {
        t1 = new Thread(()->{
            for (char c : aC) {
                while (threadNo.get() != 1) {}
                System.out.print(c);
                threadNo.set(2);
            }
        });
        t2 = new Thread(()->{
            for (char c : aI) {
                while (threadNo.get() != 2){}
                System.out.print(c);
                threadNo.set(1);
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
