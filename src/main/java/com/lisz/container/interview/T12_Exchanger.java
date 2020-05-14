package com.lisz.container.interview;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Exchanger;

public class T12_Exchanger {
    private Thread t1 = null;
    private Thread t2 = null;
    private char aC[] = "ABCDEFG".toCharArray();
    private char aI[] = "1234567".toCharArray();
    private Exchanger<String> exchanger = new Exchanger<>();

    @Test
    public void test() {
        t1 = new Thread(()->{
            for (char c : aC) {
                try {
                    System.out.print(c);
                    exchanger.exchange("T1");
                    exchanger.exchange("T1");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t2 = new Thread(()->{
            for (char c : aI) {
                try {
                    exchanger.exchange("T2");
                    System.out.print(c);
                    exchanger.exchange("T2");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
