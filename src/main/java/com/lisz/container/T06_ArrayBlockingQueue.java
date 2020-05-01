package com.lisz.container;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

// Queue和List的区别就在于它提供了offer、peek、poll等线程有好的API，而子类BlockingQueue又提供了put和take，实现了阻塞操作，支持了生产者消费者模型
// 而这个模型就是MQ的基础，MQ本质上就是一个大型的生产者消费者模型
public class T06_ArrayBlockingQueue {
    private static BlockingQueue<String> strs = new ArrayBlockingQueue<>(10);

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            strs.put("a" + i);
        }
        //strs.put("aaa"); //满了，阻塞
        //strs.add("aaa");
        //System.out.println(strs.offer("aaa"));
        strs.offer("aaa", 1, TimeUnit.SECONDS);//尝试一秒钟，加不进去再返回false
        System.out.println(strs);
    }
}
