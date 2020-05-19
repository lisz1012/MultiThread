package com.lisz.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/*
并发（Concurrent）与并行（Parallel）
并发指任务提交，并行是指任务运行。并行是并发的子集。并发是好多任务涌过来，并行是指多个CPU真的在同时处理。
而FixedThreadPool是真正的可以让任务被并行执行的
 */
public class T11_FixedThreadPool_2 {
    public static void main(String[] args) throws Exception {

        long start = System.currentTimeMillis();
        getPrime(1, 200000);
        System.out.println(System.currentTimeMillis() - start);

        // 计算密集性，就是核心数，咱的机器可以设为8 ^_^
        final int cpuCoreNum = 4; // 不是平均分布在4个核心上，因为OS还有很多其他线程，他们地位均等
        // 所有的线程都要遵守OS的线程调度策略。JVM里的一个线程就对应OS的一个线程（纤程出来之后就不一定了）
        // Linux的调度策略有三种：1 优先级 2 按时间片 3 实时。 Linux服务器上是按时间片的。虽然JVM里面
        // 指定的优先级是没有什么作用的，但是到了OS这个层面是有作用的，好多内核线程是有很高优先级的，Linux
        // 会优先执行紧急的线程，咱这些线程放在哪个CPU上还真不一定
        ExecutorService service = Executors.newFixedThreadPool(cpuCoreNum);

        /*CompletableFuture f1 = CompletableFuture.supplyAsync(()->getPrime(1, 80000));
        CompletableFuture f2 = CompletableFuture.supplyAsync(()->getPrime(80001, 130000));
        CompletableFuture f3 = CompletableFuture.supplyAsync(()->getPrime(130001, 170000));
        CompletableFuture f4 = CompletableFuture.supplyAsync(()->getPrime(170001, 200000));

        start = System.currentTimeMillis();
        CompletableFuture.allOf(f1, f2, f3, f4).join();
        System.out.println(System.currentTimeMillis() - start);*/

        MyTask t1 = new MyTask(1, 80000);
        MyTask t2 = new MyTask(80001, 130000);
        MyTask t3 = new MyTask(130001, 170000);
        MyTask t4 = new MyTask(170001, 200000);


        Future<List<Integer>> f1 = service.submit(t1); // submit是异步的
        Future<List<Integer>> f2 = service.submit(t2);
        Future<List<Integer>> f3 = service.submit(t3);
        Future<List<Integer>> f4 = service.submit(t4);

        start = System.currentTimeMillis();
        f1.get();
        f2.get();
        f3.get();
        f4.get();
        System.out.println(System.currentTimeMillis() - start);
    }

    private static class MyTask implements Callable<List<Integer>> {
        private int start;
        private int end;

        public MyTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public List<Integer> call() throws Exception {
            return getPrime(start, end);
        }
    }

    private static boolean isPrime(int n) {
        for (int i = 1; i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    private static List<Integer> getPrime(int start, int end) {
        List<Integer> list = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            if (isPrime(i)) {
                list.add(i);
            }
        }
        return list;
    }
}
/*居然是：
4
0
并行很快，尤其是在我的机器上，哈哈
 */