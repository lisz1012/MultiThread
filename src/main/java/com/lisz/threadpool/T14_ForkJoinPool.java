package com.lisz.threadpool;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

// 为什么用WorkStealingPool不直接用ForkJoinPool？提供一个更方便使用的接口，指定好了一些参数
public class T14_ForkJoinPool {
    private static int nums[] = new int[1000000];
    private static final int MAX_NUM = 50000;
    private static Random r = new Random();
    static {
        for (int i = 0; i < nums.length; i++) {
            nums[i] = r.nextInt(100);
        }
        System.out.println(Arrays.stream(nums).sum());
    }

    private static class AddAction extends RecursiveAction {
        private int start;
        private int end;

        public AddAction(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            if (end - start <= MAX_NUM) {
                int sum = 0;
                for (int i = start; i <= end; i++) {
                    sum += nums[i];
                }
                System.out.println("From: " + start + " to: " + end + " = " + sum);
            } else {
                int mid = start + ((end - start) >> 1);
                AddAction t1 = new AddAction(start, mid);
                AddAction t2 = new AddAction(mid + 1, end);
                t1.fork();
                t2.fork();
            }
        }
    }

    private static class AddTask extends RecursiveTask<Integer> {
        private int start;
        private int end;

        public AddTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            if (end - start <= MAX_NUM) {
                int sum = 0;
                for(int i = start; i <=end; i++) {
                    sum += nums[i];
                }
                return sum;
            }

            int mid = start + ((end - start) >> 1);
            AddTask t1 = new AddTask(start, mid);
            AddTask t2 = new AddTask(mid + 1, end);
            t1.fork(); // fork()会往工作队列里面扔这个任务t1本身
            t2.fork();
            return t1.join() + t2.join();
        }
    }

    public static void main(String[] args) throws Exception {
        ForkJoinPool pool = (ForkJoinPool) Executors.newWorkStealingPool();
        /*AddAction action = new AddAction(0, nums.length - 1);
        pool.execute(action);
        System.in.read();*/

        AddTask task = new AddTask(0, nums.length - 1);
        //源码中join -> doJoin -> doExec -> exec -> compute(模版方法)
        //System.out.println(pool.submit(task));
        pool.execute(task); // 要往池子里扔
        System.out.println(task.join()); // 要join
        //
    }
}
