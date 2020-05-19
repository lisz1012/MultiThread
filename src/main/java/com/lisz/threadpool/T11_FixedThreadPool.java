package com.lisz.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
什么时候用Fix什么时候用Cached？
严格来讲没有一定之规，预估应该启动的线程数，高并发场景下压测决定。压测（和测开）
N(threads) = N(CPU) * U(CPU) * (1 + W/C) where N(CPU) 是有多少个核心，and U(CPU)
是期望的CPU利用率（除了你这个程序，别的程序不也在使用CPU吗？同学们），W/C 是等待时间和计算时间的比率
没有IO操作的时候W可以几乎为0，有IO和网络操作的时候W就不可忽略了，知道这个公式想用好也不容易。最终直接
看压测结果吧，不满意再调整。W/C比较高倾向于IO（wait）密集，反之计算密集。也可以粗略地算成CPU核数+1
压测结果说明一切。

任务量不是很平稳，忽高忽低，但要保证肯定有人来执行任务，这时候要用Cached，保证不堆积；任务来的比较平稳
就用Fixed，阿里的想法是都不用，自己去估算，然后精确地设定
 */
public class T11_FixedThreadPool {
    public static void main(String[] args) throws Exception {
        ExecutorService service = Executors.newFixedThreadPool(2);
        System.out.println(service);
        for (int i = 0; i < 4; i++) {
            service.execute(()->{
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            System.out.println(service);
        }
        System.out.println(service);
        service.shutdown();
        System.out.println(service);
        TimeUnit.SECONDS.sleep(2);
        System.out.println(service);
    }
}
/*刚初始化的时候其实线程数是0，用到了才创建懒加载.shutDown会等待所有的线程结束了才优雅地结束线程池
java.util.concurrent.ThreadPoolExecutor@776ec8df[Running, pool size = 0, active threads = 0, queued tasks = 0, completed tasks = 0]
java.util.concurrent.ThreadPoolExecutor@776ec8df[Running, pool size = 1, active threads = 1, queued tasks = 0, completed tasks = 0]
java.util.concurrent.ThreadPoolExecutor@776ec8df[Running, pool size = 2, active threads = 2, queued tasks = 0, completed tasks = 0]
java.util.concurrent.ThreadPoolExecutor@776ec8df[Running, pool size = 2, active threads = 2, queued tasks = 1, completed tasks = 0]
java.util.concurrent.ThreadPoolExecutor@776ec8df[Running, pool size = 2, active threads = 2, queued tasks = 2, completed tasks = 0]
java.util.concurrent.ThreadPoolExecutor@776ec8df[Running, pool size = 2, active threads = 2, queued tasks = 2, completed tasks = 0]
java.util.concurrent.ThreadPoolExecutor@776ec8df[Shutting down, pool size = 2, active threads = 2, queued tasks = 2, completed tasks = 0]
java.util.concurrent.ThreadPoolExecutor@776ec8df[Terminated, pool size = 0, active threads = 0, queued tasks = 0, completed tasks = 4]
 */
