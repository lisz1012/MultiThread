package com.lisz.comtainer.vector_to_queue;

import java.util.Vector;
import java.util.concurrent.TimeUnit;

// 还是有问题，两把小锁（原子性操作tickets.isEmpty()和tickets.remove()）但是中间没有上锁，整个操作不是原子性的
public class TicketSeller2 {
    static Vector<String> tickets = new Vector<>();
    static {
        for (int i = 0; i < 1000; i++) {
            tickets.add("票编号： " + i);
        }
    }
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                while (!tickets.isEmpty()) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("销售了：" + tickets.remove(0));
                }
            }).start();
        }
    }
}
