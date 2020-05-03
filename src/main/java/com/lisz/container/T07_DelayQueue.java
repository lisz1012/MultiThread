package com.lisz.container;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

// 用来在时间上做任务调度，本质上用了一个PriorityQueue
public class T07_DelayQueue {
    private static BlockingQueue<MyTask> tasts = new DelayQueue<>();
    private static Random r = new Random();

    private static class MyTask implements Delayed {
        private String name;
        private long runningTime;

        public MyTask(String name, long runningTime) {
            this.name = name;
            this.runningTime = runningTime;
        }

        // 返回还有多长时间才会执行
        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(runningTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            if (getDelay(TimeUnit.MILLISECONDS) < o.getDelay(TimeUnit.MILLISECONDS)) {
                return -1;
            } else if (getDelay((TimeUnit.MILLISECONDS)) > o.getDelay(TimeUnit.MILLISECONDS)) {
                return 1;
            } else {
                return 0;
            }
        }

        @Override
        public String toString() {
            return "MyTask{" +
                    "name='" + name + '\'' +
                    ", runningTime=" + runningTime +
                    '}';
        }
    }

    public static void main(String[] args) throws  InterruptedException {
        long now = System.currentTimeMillis();
        MyTask t1 = new MyTask("t1", now + 1000);
        MyTask t2 = new MyTask("t2", now + 2000);
        MyTask t3 = new MyTask("t3", now + 1500);
        MyTask t4 = new MyTask("t4", now + 2500);
        MyTask t5 = new MyTask("t5", now + 500);

        tasts.put(t1);
        tasts.put(t2);
        tasts.put(t3);
        tasts.put(t4);
        tasts.put(t5);

        System.out.println(tasts);

        for (int i = 0; i < 5; i++) {
            System.out.println(tasts.take());
        }
    }
}
