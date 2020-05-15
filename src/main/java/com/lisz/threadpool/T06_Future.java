package com.lisz.threadpool;

import java.util.concurrent.*;

public class T06_Future {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        FutureTask<Integer> task = new FutureTask<>(()->{
            TimeUnit.MILLISECONDS.sleep(500);
            return 1000;
        });
        new Thread(task).start();

        System.out.println(task.get());

        ExecutorService service = Executors.newFixedThreadPool(5);
        Future<Integer> f = service.submit(()->{
            TimeUnit.SECONDS.sleep(2);
            return 100;
        });
        System.out.println(f.get());
        System.out.println(f.isDone());  // true
        System.out.println(service.isShutdown());  // false
        service.shutdown();
        System.out.println(service.isShutdown());  // true
    }
}
