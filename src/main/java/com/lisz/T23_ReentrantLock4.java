package com.lisz;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class T23_ReentrantLock4 {
	Lock lock = new ReentrantLock();
	
	synchronized void m1() { // 用synchronized的话，两把锁不是同一把锁，两线程互不干扰锁，锁定对象不同
		try {
			TimeUnit.SECONDS.sleep(3);
			System.out.println("m1 ...");
		} catch (InterruptedException e) {
			e.printStackTrace();
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
		T23_ReentrantLock4 t = new T23_ReentrantLock4();
		new Thread(t::m1, "t1").start();
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		new Thread(t::m2, "t2").start();
	}

}
/*
m2 ... false
m1 ...
*/