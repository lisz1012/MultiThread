package com.lisz.container.interview;

import org.junit.jupiter.api.Test;

public class T03_Sync_Wait_Notify {
    private Thread t1 = null;
    private Thread t2 = null;
    private char aI[] = "1234567".toCharArray();
    private char aC[] = "ABCDEFG".toCharArray();
    private volatile boolean t1Started = false;  // volatile 必须有

    @Test
    public void test() {
        Object o = new Object();
        t1 = new Thread(()->{
            synchronized (o) {
                t1Started = true;
                for (char c : aC) {
                    System.out.print(c);
                    o.notify();
                    try {
                        o.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                o.notify(); // 一个结束的时候另一个在wait，notify一下以便大家都结束
            }
        });
        t2 = new Thread(()->{
            synchronized (o) {
                if (!t1Started) {
                    try {
                        o.wait();  // 先拿到锁也给我释放咯
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                for (char c : aI) {
                    System.out.print(c);
                    o.notify();
                    try {
                        o.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                o.notify(); // 一个结束的时候另一个在wait，notify一下以便大家都结束
            }
        });
        t2.start();
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t1.start();


        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
