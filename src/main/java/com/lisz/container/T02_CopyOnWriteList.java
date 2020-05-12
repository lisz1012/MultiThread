package com.lisz.container;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

// 使用场景：写的特别多但是读的比较少。CopyOnWriteList读的时候不加锁，原来的时候要停掉读线程才能往里写
// 但是这里写的时候，在原来的基础上copy出一个List来，但是会扩展出一个新元素出来，然后把新添加的这个元素
// 放到最后这个位置上，然后把指向老的容器的引用指向新的容器。写效率比较低。只有往里加的时候才需要加锁，因为
// 复制了，复制出来的跟老的是一模一样的。更好的例子是调用add(int index, E element)本质上是一种read/write
// lock的概念。读的时候是共享锁，写的时候是排他锁
public class T02_CopyOnWriteList {
    public static void main(String[] args) {
        List<String> list = new CopyOnWriteArrayList<>();
        Thread ths[] = new Thread[100];
        Random r = new Random();
        for (int i = 0; i < ths.length; i++) {
            ths[i] = new Thread(()->{
                list.add("a" + r.nextInt(10000));
            });
        }
        runAndComputeTime(ths); // 47ms

        List<String> list2 = new ArrayList<>();
        for (int i = 0; i < ths.length; i++) {
            ths[i] = new Thread(()->{
                list.add("a" + r.nextInt(10000));
            });
        }
        runAndComputeTime(ths); // 11ms
    }

    private static void runAndComputeTime(Thread[] ths) {
        long start = System.currentTimeMillis();
        for (Thread th : ths) {
            th.start();
        }
        for (Thread th : ths) {
            try {
                th.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(System.currentTimeMillis() - start);
    }
}
