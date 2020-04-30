package com.lisz.lock;

import java.util.concurrent.TimeUnit;

// 打印：null。原因是用ThreadLocal的时候，设置的这个值是线程独有的：每个线程set的时候都是set的自己能访问到的那个Person
// 因为 Thread 对象里面有一个ThreadLocal.ThreadLocalMap（threadLocals成员变量），每次set的都是当前Thread的这个ThreadLocal.ThreadLocalMap
// 中的ThreadLocal这个Key所指向的值。多线程的情况下可能有一个ThreadLocal对象，在好多Thread对象的ThreadLocal.ThreadLocalMap
// 类型的引用中以Key的形式出现，指向各自线程的那个Person对象。两个线程有各自的Map，所以一个set之后另一个读不到修改后的值
// Spring/Hibernate的声明式事务中用过。声明式事务可以把事务写在配置文件里，他管理了一系列的方法：method1：去配置文件拿到数据库连接；method2、
// method3...也是做同样的事，声明式事务可以把这几个方法合到一起视为一个完整的事务，如果这些方法中，每一个拿的连接拿的不是同一个对象，这样肯定不能
// 形成一个完整的事务。如何保证在不同的方法中拿到的是同一个Connection呢？就把某个链接放在线程本地的threadLocals里，以后再拿的时候从threadLocals里
// 用ThreadLocal对象为key取出。第一个method放进去，后面的就不从线程池中取了
public class T47_ThreadLocal2 {
    static ThreadLocal<Person> tl = new ThreadLocal<>();

    public static void main(String[] args) {
        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(tl.get());
        }).start();

        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tl.set(new Person());
        }).start();
    }

    static class Person {
        String name = "zhangsan";
    }
}
