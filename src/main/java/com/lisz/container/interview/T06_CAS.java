package com.lisz.container.interview;

import org.junit.jupiter.api.Test;

// while自旋会占用CPU，await/wait等不占用
public class T06_CAS {
    private enum ReadyToRun {T1, T2}
    private volatile ReadyToRun r = ReadyToRun.T1; //保证线程可见性，立马就通知，否则可能浪费CPU时间
    private Thread t1 = null;
    private Thread t2 = null;
    private char aC[] = "ABCDEFG".toCharArray();
    private char aI[] = "1234567".toCharArray();

    @Test
    public void test() {
        t1 = new Thread(()->{
            for (char c : aC) {
                while (r != ReadyToRun.T1) {/*自旋, 占用CPU了*/}
                System.out.print(c);
                r = ReadyToRun.T2;
            }
        });
        t2 = new Thread(()->{
            for (char c : aI) {
                while (r != ReadyToRun.T2){}
                System.out.print(c);
                r = ReadyToRun.T1;
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
