package com.lisz.jmh;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PS {
    private static List<Integer> nums = new ArrayList<Integer>();
    static {
        Random r = new Random();
        for (int i = 0; i < 10000; i++) {
            nums.add(100000 + r.nextInt(1000000));
        }
    }

    static void foreach() {
        nums.forEach(v->isPrime(v));
    }

    // 在parallelStream中，多个线程采用forkJoin的方式并行处理
    static void parallel() {
        nums.parallelStream().forEach(PS::isPrime);
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
