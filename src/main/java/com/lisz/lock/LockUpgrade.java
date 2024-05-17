package com.lisz.lock;

import org.openjdk.jol.info.ClassLayout;

public class LockUpgrade {
    public static void main(String[] args) throws  Exception {
        Thread.sleep(5000);
        Object o = new Object();
        String s = ClassLayout.parseInstance(o).toPrintable();
        System.out.println(s);

        synchronized (o) {
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
        }

        Thread.sleep(10000);
        o = new Object();
        System.out.println(ClassLayout.parseInstance(o).toPrintable());
    }
}
