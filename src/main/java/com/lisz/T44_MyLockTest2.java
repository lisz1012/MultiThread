package com.lisz;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
// AQS最核心的是volatile int state这个变量，表示被同一个线程重入了多少次每入一次+1，每释放一次-1，大于0表示锁被某个线程获得，减到0就表示释放了
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


