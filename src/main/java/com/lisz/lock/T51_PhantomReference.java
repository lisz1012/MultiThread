package com.lisz.lock;

import com.lisz.utils.M;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.LinkedList;
import java.util.List;

/*
虚引用就是处理堆外内存的（基本没用，这就不是给程序员用的，是给写JVM的人用的）
phantomReference -  -  -  -> M  (箭头是虚引用)
        |
        -  -  -  -> QUEUE   (箭头是虚引用)
一旦虚引用被回收，会被装到队列里。垃圾回收的时候一旦把这个虚引用给回收的时候，会装到这个队列里让你接到一个通知，一旦检测到
队列里有一个引用存在了则说明这个虚一引用被回收啦。虚引用在GC的时候一定会被干掉，而且最关键的是你会收到一个通知，通知的方式
是：往这个Queue里扔进一个值去
这里运行的时候也要设置VM 参数：-Xms20M -Xmx20M
phantomReference.get()拿不到虚引用的值，写JVM的人会在对象被回收之后，poll出来那个被回收的引用作出相应的处理，比如：
在NIO中有个比较新的buffer，叫DirectByteBuffer，直接内存，是不被JVM虚拟机管理的内存，它是被OS直接管理的，又叫做堆外内存，
这个DirectByteBuffer是可以指向堆外内存的对象的，而他的引用一旦为null是不能被JVM回收的，因为他在堆之外。自己写Netty的时候，
可能会用虚引用回收堆外内存。堆外内存怎么回收？C或者C++写的虚拟机的话，当然是delete或者free。Java里面现在也能提供堆外内存的
回收，负责回收的这个类叫"Unsafe", 在JUC底层好多用的都是他，尤其是CAS（比如AtomicInteger的方法）. Unsafe.allocateMemory
和Unsafe.freeMemory分别用来手动分配和回收内存。DirectByteBuffer 也可以指向堆内的内存，比如本程序中的new M()对象，但是
get不到，所以实际中是不会用这个虚引用的
 */
public class T51_PhantomReference {
    private static final List<Object> LIST = new LinkedList<>();
    private static final ReferenceQueue<M> QUEUE = new ReferenceQueue<>(); //一个装着引用的队列

    public static void main(String[] args) {
        PhantomReference<M> phantomReference = new PhantomReference<>(new M(), QUEUE); //M是对象没啥实际的用，第二个参数必须是一个队列
        new Thread(()->{
            while (true) {
                LIST.add(new byte[1024*1024]); // 不断分配对象，触发GC
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
                System.out.println(phantomReference.get());//虚引用想拿里面的值，永远get不到，直接返回null
            }
        }).start();

        new Thread(()->{
            while (true) {
                Reference<? extends M> poll = QUEUE.poll();
                if (poll != null) { //写JVM的人会在对象被回收之后作出相应的处理
                    System.out.println("--- 虚引用对象被jvm回收了 --- " + poll);
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
