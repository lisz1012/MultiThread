package com.lisz.container;

import java.util.PriorityQueue;

// PriorityQueue并不是先进先出，而是排序了的。内部是一个树的实现，数组实现的最小堆
public class T08_PriorityQueue {
    public static void main(String[] args) {
        PriorityQueue<String> q = new PriorityQueue<>();
        q.offer("c");
        q.offer("e");
        q.offer("a");
        q.offer("d");
        q.offer("z");

        while (!q.isEmpty()) {
            System.out.println(q.poll());
        }
    }
}
