package com.lisz;

import com.lisz.utils.MyLock;

// AQS最核心的是volatile int state这个变量，表示被同一个线程重入了多少次: 最开始是0，每入一次+1，每释放一次-1，大于0表示锁被某个线程获得，减到0就表示释放了。
// AQS的核心就是一个共享的数据：state，和一堆（双向链表存储的）互相抢夺的线程。用CAS操作head和tail替代整条链表的操作
// VarHandle： 是指向某个变量的引用。Object o = new Object(); 已经有一个引用o指向new Object了，那为什么还要有另外有个VarHandle的引用也指向他呢？
//
public class T44_MyLockTest2 {
    public static int m = 0;
    public static MyLock lock = new MyLock();

    public static void main(String[] args) {
        Thread threads[] = new Thread[100];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(()->{
                try {
                    lock.lock();
                    for (int j = 0; j < 100; j++) m++;
                } finally {
                    lock.unlock();
                }
            });
        }
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(m);
    }
}


