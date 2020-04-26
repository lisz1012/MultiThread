package com.lisz.utils;

public class M {
    /*
     重写已废弃的引用：为的是了解在垃圾回收的时候，各种不同的引用他们的表现。
     在GC的时候finalize方法是要被调用的。重写这个方法就可以观察到某个对象
     什么时候被垃圾回收了。工作中永远不需要被重写或者调用
     */
    @Override
    protected void finalize() throws Throwable {
        System.out.println("Finalize");
    }
}