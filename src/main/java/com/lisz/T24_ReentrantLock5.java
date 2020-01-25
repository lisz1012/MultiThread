package com.lisz;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class T24_ReentrantLock5 {
	private static Lock lock = new ReentrantLock();
	
	void m1() {
		try {
			lock.lock();
			System.out.println("m1 started...");
			TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
			System.out.println("m1 ended");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	void m2() {
		try {
			// lock.lock(); //用这种方式是不能被打断的，仍然会继续等待
			lock.lockInterruptibly();      //阻塞等待锁的时候可以响应Thread.interrupt()并抛出异常，代码转到catch block
			System.out.println("m2 got the lock");
		} catch (InterruptedException e) {
			System.out.println("m2 did not get the lock");
		}
	}
	
	public static void main(String[] args) {
		T24_ReentrantLock5 t = new T24_ReentrantLock5();
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
		thread.interrupt();
	}

}
