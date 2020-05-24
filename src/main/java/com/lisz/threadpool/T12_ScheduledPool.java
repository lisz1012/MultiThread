package com.lisz.threadpool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// 用的不多，简单的用一个Timer，复杂的用定时器框架Quartz、cron（涉及到shell脚本）
/*
面试题：加入提供了一个闹钟服务，订阅这个服务的人特别多，10亿，意味着每天早上7am有10亿并发量涌向服务器，如何优化？
主服务器同步到边缘服务器，多台机器工作，每台机器有线程池和任务队列，消费任务（不能做到完全准时）
直接写个app发到用户手机端，每天定时向授时服务器校准，到了7点就开始响铃.
newScheduledThreadPool(n)会返回一个ScheduledThreadPoolExecutor，后者里面有个DelayedWorkQueue，源码里面实现了
类似一个优先队列的功能：A DelayedWorkQueue is based on a heap-based data structure like those in DelayQueue
and PriorityQueue ...
 */
public class T12_ScheduledPool {
    public static void main(String[] args) {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(3);
        service.scheduleAtFixedRate(()->{
            System.out.println("ABC");
        }, 5000, 1000, TimeUnit.MILLISECONDS); //5秒后开始不断的每秒打印一个"ABC"
    }
}