package com.lisz;

import java.util.concurrent.CountDownLatch;
/*
 * 同样用到了volatile status和CAS，state在CountDownLatch中的实现是还有多少次就能解开await从而继续执行
 */
public class T27_CountDownLatch {

	public static void main(String[] args) {
		usingJoin();
		usingCountDownLatch();
	}

	private static void usingCountDownLatch() {
		Thread threads[] = new Thread[100];
		CountDownLatch latch = new CountDownLatch(threads.length);
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(()->{
				int result = 0;
				for (int j = 0; j < 10000; j++) {
					result += j;
				}
				latch.countDown(); //原子操作
			});
		}
		
		for (int i = 0; i < threads.length; i++) {
			threads[i].start();
		}
		
		try {
			latch.await(); //门闩插住不要动，直到数到0, 代码才继续运行,CountDownLatch比join更灵活，可以在同一个线程里不断count down，控制是否继续往下走
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("End latch");
	}

	private static void usingJoin() {
		Thread threads[] = new Thread[100];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(()->{
				int result = 0;
				for (int j = 0; j < 10000; j++) {
					result += j;
				}
			});
		}
		
		for (int i = 0; i < threads.length; i++) {
			threads[i].start();
		}
		
		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("End latch");
	}

}
