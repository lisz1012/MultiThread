package com.lisz.container.interview;

import org.junit.jupiter.api.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class T05_Lock_Condition {
    private Thread t1 = null;
    private Thread t2 = null;
    private char aI[] = "1234567".toCharArray();
    private char aC[] = "ABCDEFG".toCharArray();
    private Lock lock = new ReentrantLock();
    private Condition condition1 = lock.newCondition();
    private Condition condition2 = lock.newCondition();
    private volatile boolean t1Started = false;

    @Test
    public void test() {
        t1 = new Thread(()->{
            try {
                lock.lock();
                for (char c : aC) {
                    t1Started = true;
                    System.out.print(c);
                    condition2.signal();
                    condition1.await();
                }
                condition2.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });
        t2 = new Thread(()->{
            try {
                lock.lock();
                if (!t1Started) {      // 如果t2是消费者线程且有多个，这里可能就得用while
                    condition2.await();
                }
                for (char c : aI) {
                    System.out.print(c);
                    condition1.signal();
                    condition2.await();
                }
                condition1.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
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
