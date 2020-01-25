package com.lisz;

import java.util.concurrent.TimeUnit;

public class T25_ReentrantLock6 {
	
	synchronized void m1() {
		try {
			System.out.println("m1 started...");
			TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
			System.out.println("m1 ended");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	synchronized void m2() {
		System.out.println("m2 got the lock");
	}
	
	public static void main(String[] args) {
		T25_ReentrantLock6 t = new T25_ReentrantLock6();
		new Thread(t::m1).start();
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Thread thread = new Thread(t::m2);
		thread.start();
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		thread.interrupt(); //相比上一个程序，这里没用ReentrantLock，而是用了synchronized，机会一直等，interrupt也不管用
	}

}