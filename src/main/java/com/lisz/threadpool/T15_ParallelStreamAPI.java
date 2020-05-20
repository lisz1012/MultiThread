package com.lisz.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// parallelStream里面也使用了ForkJoinPool，互相之间不需要同步的时候可以使用并行流
public class T15_ParallelStreamAPI {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 10000000; i++) {
            list.add(r.nextInt(1000000) + 1000000);
        }
        long start = System.currentTimeMillis();
        list.forEach(i->isPrime(i));
        System.out.println(System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        list.parallelStream().forEach(T15_ParallelStreamAPI::isPrime);
        System.out.println(System.currentTimeMillis() - start);
    }

    private static boolean isPrime(int n) {
        for (int i = 1; i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}
/*
打印居然是
25
81
 */