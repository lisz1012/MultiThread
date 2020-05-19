package com.lisz.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Executors是个线程池的工厂。这里只是产生一个线程的线程池。为什么要有这么一个线程池？而不是自己new？因为new Thread有开销，每次都new有消耗
// 线程池有任务队列的，自己new Thread的话要自己维护任务队列。线程池能提供线程的生命周期管理。阿里是不建议用JDK自带的线程池的，因为LinkedBlockingQueue
// 的大小Integer.MAX_VALUE（秒杀的时候，会有很多Task）和拒绝策略等都不太合适，至少对于阿里的业务来讲，还需要自定义线程名称
public class T09_SingleThreadPool {
    public static void main(String[] args) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 5; i++) {
            final int j = i;
            service.execute(()->{
                System.out.println(j + " " + Thread.currentThread().getName());
            });
        }
    }
}
/* 保证顺序
0 pool-1-thread-1
1 pool-1-thread-1
2 pool-1-thread-1
3 pool-1-thread-1
4 pool-1-thread-1
 */
