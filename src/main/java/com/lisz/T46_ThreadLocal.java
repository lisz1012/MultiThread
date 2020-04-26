package com.lisz;

import java.util.concurrent.TimeUnit;

// 这里一个线程里的修改影响到了另一个线程里的内容，如何做呢？见 T47_ThreadLocal2
public class T46_ThreadLocal {
    static volatile Person p = new Person();

    public static void main(String[] args) {
        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(p.name);
        }).start();

        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            p.name = "lisi";
        }).start();
    }

    static class Person {
        String name = "zhangsan";
    }
}
