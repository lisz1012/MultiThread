package com.lisz.threadpool;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
 WorkStealingPool就是ForkJoinPool，与ThreadPoolExecutor最大的不同是每个线程一有自己一个单独的队列。当某一个线程执行完
 自己的任务的时候，他就会去别的线程队列里面偷.ThreadPoolExecutor里面的线程从一个公共队列里拿任务
 对于每个队列，push和pop是不需要加锁的，而poll需要，可能多个线程来偷同一个队列的任务
 */
public class T13_WorkStealingPool {
    public static void main(String[] args) {
        ExecutorService service = Executors.newWorkStealingPool();
        System.out.println(Runtime.getRuntime().availableProcessors());
        service.execute(new R(1000));
        service.execute(new R(2000));
        service.execute(new R(2000));
        service.execute(new R(2000));
        service.execute(new R(2000));
        service.execute(new R(2000));
        service.execute(new R(2000));
        service.execute(new R(2000));
        service.execute(new R(2000));
        service.execute(new R(2000));
        service.execute(new R(2000));
        service.execute(new R(2000));
        service.execute(new R(2000));
        service.execute(new R(2000));
        service.execute(new R(2000));
        service.execute(new R(2000));
        service.execute(new R(2000));

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class R implements Runnable {
        private int t;

        public R(int t) {
            this.t = t;
        }

        @Override
        public void run() {
            try {
                TimeUnit.MILLISECONDS.sleep(t);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName());
        }
    }
}
/*打印如下，可见ForkJoinPool-1-worker-19偷到了任务
ForkJoinPool-1-worker-19
ForkJoinPool-1-worker-5
ForkJoinPool-1-worker-15
ForkJoinPool-1-worker-29
ForkJoinPool-1-worker-11
ForkJoinPool-1-worker-1
ForkJoinPool-1-worker-25
ForkJoinPool-1-worker-7
ForkJoinPool-1-worker-31
ForkJoinPool-1-worker-21
ForkJoinPool-1-worker-3
ForkJoinPool-1-worker-23
ForkJoinPool-1-worker-13
ForkJoinPool-1-worker-27
ForkJoinPool-1-worker-9
ForkJoinPool-1-worker-17
ForkJoinPool-1-worker-19
 */