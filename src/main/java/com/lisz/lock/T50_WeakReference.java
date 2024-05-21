package com.lisz.lock;

import com.lisz.utils.M;

import java.lang.ref.WeakReference;
/*
Java的引用有4种：强软弱虚（背过记住）。VarHandle是强引用.
1. 强引用：Object o = new Object(); o就是指向new 出来对象的强引用，就是普通的引用。只要有个
   引用指向他，垃圾回收器就一定不会回收他；只有没有引用指向他了，才会被回收
2. 软引用：GC不一定能回收掉，但当heap不够的时候，率先干掉弱引用
3. 弱引用：只要遭遇到GC就会被回收。用途（关键）：有另一个强引用指向对象的时候，只要强引用消失，
          对象就应该被回收掉，我们就不用管了。一般会被用在容器里（记住这个结论，作业：
          查看WeakHashMap，看AQS的unlock源码）一个典型的应用就是ThreadLocal：曾记否？
          ThreadLocal有个内部类叫ThreadLocalMap，每个Thread对象都有一个 threadLocals
          变量指向他，这个Map中的Entry就继承了WeakReference
4. 虚引用：给JVM或者其他的框架（如Netty）的开发者调用的，对于堆外内存的回收作出相应
 */
public class T50_WeakReference {
    public static void main(String[] args) {
        WeakReference<M> w = new WeakReference<>(new M());

        System.out.println(w.get());
        System.gc();   // 只要遭遇垃圾回收就会被回收
        System.out.println(w.get());

        /*
         tl -----> ThreadLocal
                    ^
                    |弱引用
                    |     map
                    key ------> M对象 (Key是ThreadLocal对象，Thread对象持有ThreadLocalMap对象的引用)
         而Thread的Map：threadLocals中的那个key是通过
         一个弱引用指向了ThreadLocal对象。key指向ThreadLocal对象的箭头如果换成了强引用，
         则会：方法里定义的ThreadLocal对象，方法结束之后tl消失，按理说应该回收ThreadLocal
         对象，但是会等到Thread结束，整个Map被回收之后才能清理它，有很多线程是长期存在的，
         所以就造成了内存泄漏。根本原因就是这个Map偏偏是属于Thread对象的
         弱引用的情况下，ThreadLocal 对象确实会被回收，但是map里面的key就指向了null，这样
         他就不会被访问到了，但那个作为M对象也就永远在那儿了，还是会有内存泄漏。怎么办呢？给我记住
         咯：使用ThreadLocal，里面set的那个对象不用了，必须在方法结束前手动remove掉！那value那个M
         为什么不能也是弱引用指向他呢？这样的话他可能只有一个弱引用指向，直接被回收。
         这里其实是有了一个隐藏的内存逃逸, 方法可能早已退出，但ThreadLocalMap对象与线程同寿！
         不知道将来的版本有没有类似try(){}的功能，把不用了的ThreadLocal对象自动remove掉
         */
        ThreadLocal<M> tl = new ThreadLocal<>();
        tl.set(new M());
        tl.remove(); // 这一步不做会引起内存泄漏！ThreadLocal的坑就在这里
    }
}
