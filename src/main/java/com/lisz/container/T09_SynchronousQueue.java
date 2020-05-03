package com.lisz.container;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

// 这类queue容量为0，是用来让一个线程给另外一个线程下达任务的。容量为0，本质上就是要直接递到另外一个线程手里才可以。两个线程直接交换数据
// 用出最大的一个Queue、取任务、线程调度都用它
public class T09_SynchronousQueue {
    public static void main(String[] args) throws Exception {
        BlockingQueue<String> strs = new SynchronousQueue<>();
        new Thread(()->{
            try {
                System.out.println(strs.take());//真正的这个take可能在put之前或者之后运行，但线程的开启一定要在put之前，以为没有消费者put会阻塞
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        strs.put("aaa"); // 在没有消费者在哪里阻塞着take的时候，就永远在这里等着。换成add会直接报错，因为容量为0，前面有take也不行，只能put阻塞着装任务
        System.out.println(strs.size());
    }


}
