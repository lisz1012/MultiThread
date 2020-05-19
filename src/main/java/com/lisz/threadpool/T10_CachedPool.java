package com.lisz.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//有闲着不超过60秒的线程存在就用它，没有就启动新线程。来一个Task必须有个线程拿走（或者新建一个线程），否则提交任务的线程就阻塞。
//来一个任务必须就被拿走马上执行，没有空闲的就new一个新线程执行。阿里也不建议使用这种，因为可能启动的线程数非常多，线程数要是达到
//Integer.MAX_VALUE CPU就别干别的了，光调度就调度不过来。阿里是有这个担忧的，业务量驱动创新
public class T10_CachedPool {
    public static void main(String[] args) throws Exception {
        ExecutorService service = Executors.newCachedThreadPool();
        System.out.println(service);
        for (int i = 0; i < 2; i++) {
            service.execute(()->{
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName());
            });
        }
        System.out.println(service);
        TimeUnit.SECONDS.sleep(80);
        System.out.println(service);
    }
}
/*
pool-1-thread-1
pool-1-thread-2
 */
