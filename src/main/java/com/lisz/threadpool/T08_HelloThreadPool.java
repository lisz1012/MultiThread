package com.lisz.threadpool;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

// 线程池的7个参数要背过！
public class T08_HelloThreadPool {
    private static class Task implements Runnable{
        private int i;

        public Task(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " Task " + i);
            try {
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return "Task{" +
                    "i=" + i +
                    '}';
        }
    }

    public static void main(String[] args) {
        ThreadPoolExecutor tpe = new ThreadPoolExecutor(2, // 核心线程
                                                    4, // 最大线程，任务处理不过来，最大能扩展到多少个线程处理
                                                      60, // 一段时间内不干活的线程要被归还给操作系统，线程是有开销的，不用了就归还，让OS给别人去用这个线程资源。但是核心线程在这里不归还
                                                        TimeUnit.SECONDS, // 时间单位
                                                        new ArrayBlockingQueue<Runnable>(4), // LinkedBlockingQueue，最大容量是Integer.MAX_VALUE。传SynchrounousQueue的话来了任务之后必须拿走才能加新任务
                                                        Executors.defaultThreadFactory(), // 可以自定义工厂，生产有自定义名字、是否是守护、优先级的线程。指定线程名称很重要，方便出错时回溯
                                                        new ThreadPoolExecutor.CallerRunsPolicy()); // 刚来的前两个任务，起两个核心线程，不回收；再来的进队列，队列满了再起两个线程，仍然还有新任务过来，则执行拒绝策略
        // （拒绝策略可以自定义）1.Abort,直接抛异常 2.Discard,扔掉，不抛异常。3.DiscardOldest扔掉排队时间最久的 4.CallerRuns调用者处理任务：r.run();成了同步执行了.
        // 实际中一般都要自定义策略，而不是用以上任何一种，一般需要把消息都保存下来到Kafka、Redis、XXMQ、DB，做好日志，尤其是订单信息等。当日志或者在MQ里发现有大量的任务积压，没有被处理，说明该加机器了（消费者）
        // 马老师重要启示：做轮子不做业务，写技术性强的框架或者中台
        for (int i = 0; i < 8; i++) {
            tpe.execute(new Task(i));
        }
        System.out.println(tpe.getQueue());
        tpe.execute(new Task(100));
        System.out.println(tpe.getQueue());
        tpe.shutdown();
    }
}
// 以下结果在ThreadPoolExecutor.DiscardOldestPolicy时输出，可以说明两点：1.任务分配顺序：核心线程 -> 排队 -> 外围线程. 2.2号线程是最早在任务队列里的，满了之后先剔除它，再加入新的任务。应用场景比如：（游戏）物体位置更新
/*
[Task{i=2}, Task{i=3}, Task{i=4}, Task{i=5}]
[Task{i=3}, Task{i=4}, Task{i=5}, Task{i=100}]
pool-1-thread-1 Task 0
pool-1-thread-3 Task 6
pool-1-thread-4 Task 7
pool-1-thread-2 Task 1

以下结果在ThreadPoolExecutor.CallerRunsPolicy()时输出
[Task{i=2}, Task{i=3}, Task{i=4}, Task{i=5}]
pool-1-thread-2 Task 1
pool-1-thread-3 Task 6
pool-1-thread-4 Task 7
pool-1-thread-1 Task 0
main Task 100
 */
