package com.lisz.container;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

// poll(), size(), peek(), offer()等都是线程安全的
public class T04_ConcurrentQueue {
    public static void main(String[] args) {
        Queue<String> strs = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < 10; i++) {
            strs.offer("a" + i);
        }

        System.out.println(strs);

        System.out.println(strs.size());

        System.out.println(strs.poll());
        System.out.println(strs.size());

        System.out.println(strs.peek());
        System.out.println(strs.size());
    }
}
