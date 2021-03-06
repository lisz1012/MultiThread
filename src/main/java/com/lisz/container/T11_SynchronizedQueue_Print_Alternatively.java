package com.lisz.container;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

// 面试题：用多线程且不能用synchronized，交替打印数字和字母：
// 1a2b3c4d5e6f7g8h9i10j11k12l13m14n15o16p17q18r19s20t21u22v23w24x25y26z
// 容量为0的BlockingQueue，所以任何时刻put往里放，take往外取都要阻塞
public class T11_SynchronizedQueue_Print_Alternatively {
    public static void main(String[] args) {
        BlockingQueue<Integer> q = new SynchronousQueue<>();
        new Thread(()->{
            for (char c = 'a'; c <= 'z'; c++) {
                try {
                    System.out.print(q.take());
                    q.put(c - 'a');
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(()->{
            for (int i = 1; i <=26; i++) {
                try {
                    q.put(i); // 由于容量为0，没有消费者就block在这儿
                    System.out.print((char)(q.take() + 'a'));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
