package com.lisz.comtainer.vector_to_queue;

import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

// 高并发下线性存储容器，多考虑Queue（ConcurrentLinkedQueue），少考虑List！！！（想随机访问怎么办？）
// ConcurrentLinkedQueue往外poll的时候都是CAS的无锁化的原子操作，所以效率高很多
// CAS一定就比synchronized效率高吗？不一定，要看并发量的高低、锁定的那段代码执行的时间
// 单线程一般就用HashMap、ArrayList；高并发的时候执行时间短就应该用ConcurrentHashMap、ConcurrentLinkedQueue；执行时间长，并发量又不是特别高，用synchronizedMap
// 总之要通过压测才能确定容器。面向接口的编程和设计：接口只是说明业务逻辑，下面有好几套实现，在不同的并发下用不同的实现
public class TicketSeller4 {
    static Queue<String> tickets = new ConcurrentLinkedQueue<>();
    static {
        for (int i = 0; i < 1000; i++) {
            tickets.add("票编号： " + i);
        }
    }
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                // 换成while中判断tickets是否为空、睡觉10ms且删除if (s == null) break;则还是会出前面的问题
                while (true) {
                    String s = tickets.poll();
                    if (s == null) break;
                    System.out.println("销售了 " + s);
                }
            }).start();
        }
    }
}
