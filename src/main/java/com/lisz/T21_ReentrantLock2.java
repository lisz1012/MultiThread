package com.lisz;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class T21_ReentrantLock2 {
	private Lock lock = new ReentrantLock();
	
	void m1() {
		try {
			lock.lock();
			for (int i = 0; i < 10; i++) {
				TimeUnit.SECONDS.sleep(1);
				System.out.println(i);
				if (i == 2) {
					m2();
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	private void m2() {
		try {
			lock.lock();
			System.out.println("m2 ...");
		} finally {
			lock.unlock();
		}
	}

	public static void main(String[] args) {
		T21_ReentrantLock2 t = new T21_ReentrantLock2();
		t.m1();
	}

}
