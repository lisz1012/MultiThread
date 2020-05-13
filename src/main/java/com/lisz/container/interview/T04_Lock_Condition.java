package com.lisz.container.interview;

import org.junit.jupiter.api.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class T04_Lock_Condition {
    private Thread t1 = null;
    private Thread t2 = null;
    private char aI[] = "1234567".toCharArray();
    private char aC[] = "ABCDEFG".toCharArray();
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private volatile boolean t1Started = false;

    @Test
    public void test() {
        t1 = new Thread(()->{
            try {
                lock.lock();
                for (char c : aC) {
                    t1Started = true;
                    System.out.print(c);
                    condition.signal();
                    condition.await();
                }
                condition.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });
        t2 = new Thread(()->{
            try {
                lock.lock();
                if (!t1Started) {
                    condition.await();
                }
                for (char c : aI) {
                    System.out.print(c);
                    condition.signal();
                    condition.await();
                }
                condition.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });

        t2.start();
        t1.start();


        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
