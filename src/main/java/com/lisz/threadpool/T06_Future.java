package com.lisz.threadpool;

import java.util.concurrent.*;

public class T06_Future {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // FutureTask自己是个任务，而且运行结果也可以存在他里面，其实他比Future更好用，Runnable和Future的结合
        // Future存储执行的将来才会产生的结果
        FutureTask<Integer> task = new FutureTask<>(()->{ // 像Runnable
            TimeUnit.MILLISECONDS.sleep(500);
            return 1000;
        });
        new Thread(task).start();  // 像Runnable

        System.out.println(task.get()); //像Future

        ExecutorService service = Executors.newFixedThreadPool(5);
        Future<Integer> f = service.submit(()->{
            TimeUnit.SECONDS.sleep(2);
            return 100;
        });
        System.out.println(f.isDone());  // false
        System.out.println(f.get());  // 阻塞，直到有结果get才会返回
        System.out.println(f.isDone());  // true
        System.out.println(service.isShutdown());  // false
        service.shutdown();
        System.out.println(service.isShutdown());  // true
    }
}
