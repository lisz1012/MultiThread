package com.lisz;

import java.util.concurrent.TimeUnit;
/*
 * 程序中如果出现异常，默认情况下锁会被释放。原来在等待锁的线程乱入了
 * Webapp中，多个Servlet会共同访问同一个资源这是如果一场处理不合适，
 * 在第一个线程中抛出异常，其他线程就会进入同步代码区，有可能访问到异
 * 常产生时的数据
 */
public class T12_synchronized_10 {
	private int count = 0;
	
	public synchronized void m() {
		System.out.println(Thread.currentThread().getName() + " start");
		while (true) {
			count ++;
			System.out.println(Thread.currentThread().getName() + " count = " + count);
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (count == 5) {
				int i = count / 0;
				System.out.println(i);
			}
		}
	}
	public static void main(String[] args) {
		T12_synchronized_10 t = new T12_synchronized_10();
		new Thread(()->{
			t.m();
		}, "t1").start();
		new Thread(()->{
			t.m();
		}, "t2").start();
	}

}
