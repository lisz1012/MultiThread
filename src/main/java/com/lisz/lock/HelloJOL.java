package com.lisz.lock;

import org.openjdk.jol.info.ClassLayout;

public class HelloJOL {
    private static class T {
        int m = 8;
        String s = "hello";
    }

    public static void main(String[] args) {
        Object o = new Object();
        String s = ClassLayout.parseInstance(o).toPrintable();
        System.out.println(s);

//        T t = new T();
//        String s2 = ClassLayout.parseInstance(t).toPrintable();
//        System.out.println(s2);
        synchronized (o) {
            String s3 = ClassLayout.parseInstance(o).toPrintable();
            System.out.println(s3);
        }
    }
}
