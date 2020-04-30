package com.lisz.lock;

import java.lang.ref.SoftReference;

/*
m -----------> SoftReference - - - - -> byte[10M]
被软引用指向的对象（这里的byte[]）只有在heap内存不够用的情况下才会回收
运行这个小程序的时候要设置VM options： -Xms20M -Xmx20M 堆内存只有20M
（在生产环境下，最大和最小堆空间都设成一样大。）
软引用的用途主要是缓存，比如从硬盘中读出来一个大图片，想让别人加速访问，
不用的时候又可以干掉。或者是从数据库里拿出来的大对象或者数据，这个数据用
一下get，还能访问到，那就不用再去数据库里拿了。需要新的空间，请干掉我，
不需要的话。一般我们也用不上，做缓存也是调用Redis
 */
public class T49_SoftReference {
    public static void main(String[] args) {
        SoftReference<byte[]> m = new SoftReference<>(new byte[1024 * 1024 * 10]);
        System.out.println(m.get());
        //System.gc(); // <------- Full GC，调用了也不确定会立刻回收
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(m.get()); //一共20M的堆，只用了10M所以不会被回收

        // 再分配一个数组，heap将装不下，这时候系统会垃圾回收，先回收一次，如果不够，会把软引用干掉，不用显式调用System.gc()
        byte b[] = new byte[1024 * 1024 * 15]; //内存不够用，强引用会挤掉软引用。先gc清理掉软引用，再分配
        System.out.println(m.get());
    }
}
