package com.lisz;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class T43_MyLockTest {
    public static int m = 0;
    public static Lock lock = new MyLock();

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

    private static class MyLock implements Lock {
        private volatile int i = 0;

        @Override
        public void lock() {
            //模仿CAS
            synchronized (this) {
                while (i != 0) {
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                i = 1;
            }
        }

        @Override
        public void lockInterruptibly() throws InterruptedException {

        }

        @Override
        public boolean tryLock() {
            return false;
        }

        @Override
        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            return false;
        }

        @Override
        public void unlock() {
            i = 0;
        }

        @Override
        public Condition newCondition() {
            return null;
        }
    }
}


