package com.lisz;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class T26_ReentrantLock7 {
	// 公平锁, 先到先得，等待中的线程在一个队列里。非公平锁不检查队列里有没有线程，直接抢；公平锁反之，要进队列排队。默认非公平锁。synchronized只有非公平锁
	// ReentrantLock也是用到了CAS。除了synchronized之外，多数用的都是CAS
	Lock lock = new ReentrantLock(true); 

	void m() {
		for (int i = 0; i < 100; i++) {
			try {
				lock.lock();
				System.out.println(Thread.currentThread().getName());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			} 
		}
	}
	
	public static void main(String[] args) throws Exception {
		T26_ReentrantLock7 t = new T26_ReentrantLock7();
		new Thread(t::m, "t1").start();
		new Thread(t::m, "t2").start();
	}
}
/*
t1
t1   
t1
t1
t1     t2还在准备的时候t1已经执行了
t1
t1
t1
t1
t1
t1
t1
t1
t1
t2      大概相间执行
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t1
t2
t2
t2
t2
t2
t2
t2
t2
t2
t2
t2
t2
t2
t2
 
*/