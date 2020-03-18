package com.lisz;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/*
 * 每个Condition就是一个独立的等待队列。当producer.await();的时候，当前线程进入producer的那个等待队列进行等待
 * 当consumer.await();的时候，当前线程进入consumer的那个等待队列进行等待signal可以只叫醒某个队列里的线程
 */

public class T42_SynchronizedContainer2 {
	private int capacity;
	private List<Integer> list = new ArrayList<>();
	private ReentrantLock lock = new ReentrantLock();
	private Condition producer = lock.newCondition();
	private Condition consumer = lock.newCondition();
	
	public T42_SynchronizedContainer2(int capacity) {
		this.capacity = capacity;
	}
	
	public Integer put(int i) {
		try {
			lock.lock();
			while (list.size() == capacity) { //这个while可以不写，容器一上来是空的，上来producer不用wait，它运行的时候一定是可以被叫醒的时候。前提是consumer不会new新的producer线程
				producer.await();
			}
			list.add(i);
			if (list.size() == capacity) {
				consumer.signalAll();
			} else {
				producer.signalAll();
				consumer.signalAll();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return i;
	}
	
	public Integer get() {
		int ret = 0;
		try {
			lock.lock();
			while (list.size() == 0) {
				consumer.await();
			}
			ret = list.remove(0);
			if (list.size() == 0) {
				producer.signalAll();
			} else {
				producer.signalAll();
				consumer.signalAll();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return ret;
	}
	
	public synchronized int getCount() {
		return list.size();
	}
	
	public static void main(String[] args) {
		T42_SynchronizedContainer2 c = new T42_SynchronizedContainer2(5);
		for (int i = 0; i < 2; i++) {
			new Thread(()->{
				for (int j = 0; j < 100; j++) {
					System.out.println("Added " + c.put(j));
				}
			}).start();
		}
		
		for (int i = 0; i < 10; i++) {
			new Thread(()->{
				while (true) {
					System.out.println("Get " + c.get());
				}
			}).start();
		}
		
		new Thread(()->{
			for (int i = 0; i < 3; i++) {
				System.out.println("Count: " + c.getCount());
			}
		}).start();
		
	}

}
