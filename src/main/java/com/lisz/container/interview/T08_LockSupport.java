package com.lisz.container.interview;

import java.util.concurrent.locks.LockSupport;

public class T08_LockSupport {
    private static Thread t1 = null;
    private static Thread t2 = null;

    public static void main(String[] args) {
        char aI[] = "1234567".toCharArray();
        char aC[] = "ABCDEFG".toCharArray();
        t1 = new Thread(()->{
            for (char c : aC) {
                System.out.print(c);
                LockSupport.unpark(t2);
                LockSupport.park();
            }
        });
        t2 = new Thread(()->{
            for (char c : aI) {
                LockSupport.park();
                System.out.print(c);
                LockSupport.unpark(t1);
            }
        });
        t1.start();
        t2.start();
    }
}
