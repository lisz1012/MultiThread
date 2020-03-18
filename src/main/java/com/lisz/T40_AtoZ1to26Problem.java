package com.lisz;

import java.util.concurrent.CountDownLatch;

/**
 * 两个线程，分别打印A-Z和1-26，但是要求交叉轮番打印：A1B2...Z26
 * @author shuzheng, 一次运行通过！！！哈哈哈
 *
 */
public class T40_AtoZ1to26Problem {
	private static CountDownLatch latch1 = new CountDownLatch(1);
	private static CountDownLatch latch2 = new CountDownLatch(1);
	
	public static void main(String[] args) {
		new Thread(()->{
			char c = 'A';
			for (int i = 0; i < 26; i++) {
				latch1 = new CountDownLatch(1);
				System.out.print((char)(c + i));
				latch2.countDown();
				try {
					latch1.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			latch2.countDown();
		}, "t1").start();
		
		new Thread(()->{
			try {
				latch2.await();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			for (int i = 0; i < 26; i++) {
				latch2 = new CountDownLatch(1);
				System.out.print(i+1);
				latch1.countDown();
				try {
					latch2.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "t2").start();
	}
}
