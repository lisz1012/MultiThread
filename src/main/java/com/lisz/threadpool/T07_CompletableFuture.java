package com.lisz.threadpool;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

// CompletableFuture提供了任务堆的管理，管理多个Future的结果，各种任务的管理类
public class T07_CompletableFuture {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        priceOfTM();
        priceOfTB();
        priceOfJD();
        System.out.println("Serial call time: " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        CompletableFuture<Double> futureTM = CompletableFuture.supplyAsync(()->priceOfTM()); // Supplier.get()就返回一个priceOfTM()的返回值
        CompletableFuture<Double> futureTB = CompletableFuture.supplyAsync(()->priceOfTB()); // 开启异步任务，相当于往外甩线程
        CompletableFuture<Double> futureJD = CompletableFuture.supplyAsync(()->priceOfJD());

        CompletableFuture.supplyAsync(()->priceOfTM())
                         .thenApply(String::valueOf)
                         .thenApply(s->"price: " + s) // 把一个String转成另一个String
                         .thenAccept(System.out::println); // 依次对上一步返回的结果执行操作

        CompletableFuture.allOf(futureTM, futureTB, futureJD).join(); // 全部完成之后才join, 继续往下运行。相当于甩出3个线程然后分别执行，最后join。anyOf是任何一个执行完了就可以往下走
        System.out.println("Parallel call time: " + (System.currentTimeMillis() - start));

        try {
            System.in.read();  //阻塞住，否则"price: 1.12"打印不出来
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Double priceOfJD() {
        delay();
        return 1.1;
    }

    private static Double priceOfTB() {
        delay();
        return 1.11;
    }

    private static Double priceOfTM() {
        delay();
        return 1.12;
    }

    private static void delay() {
        Random random = new Random();
        try {
            TimeUnit.MILLISECONDS.sleep(random.nextInt(500));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
