package com.lisz;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
// 有的线程必须得等其他的都完事儿了才能继续往下执行。复杂的操作：访问数据库、网络、文件，可以让他们又不同的线程来做，它们并发执行，然后都执行完了之后才执行下面的。题外话：限流可能会用Guava 的 Ratelimiter
public class T28_CyclicBarrier {

	public static void main(String[] args) {
		CyclicBarrier barrier = new CyclicBarrier(20, ()->System.out.println("满人，发车"));
		
		for (int i = 0; i < 100; i++) {
			new Thread(()->{
				try {
					barrier.await(); // 来一个线程执行到这里就会计数，
				} catch (InterruptedException | BrokenBarrierException e) {
					e.printStackTrace();
				}
			}).start();
		}
	}
}
