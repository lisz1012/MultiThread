package com.lisz.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class T22_ReentrantLock3 {
	Lock lock = new ReentrantLock();
	
	/*synchronized*/ void m1() { // m1里面不用ReentrantLock而用synchronized的话，两把锁不是同一把锁
		try {
			lock.lock();
			TimeUnit.SECONDS.sleep(10);
			System.out.println("m1 ...");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		
	}
	
	void m2() {
		boolean locked = false;
		try {
			locked = lock.tryLock(5, TimeUnit.SECONDS);
			System.out.println("m2 ... " + locked);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (locked) {
				lock.unlock();
			}
		}
		
	}
	
	public static void main(String[] args) {
		T22_ReentrantLock3 t = new T22_ReentrantLock3();
		new Thread(t::m1, "t1").start();
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		new Thread(t::m2, "t2").start();
	}

}
