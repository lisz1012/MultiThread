package com.lisz.lock;

import java.util.ArrayList;
import java.util.List;

public class T39_SynchronizedContainer {
	private int capacity;
	private List<Integer> list = new ArrayList<>();
	
	public T39_SynchronizedContainer(int capacity) {
		this.capacity = capacity;
	}
	
	public synchronized Integer put(int i) {
		while (list.size() == capacity) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			};
		}
		list.add(i);
		notifyAll();
		return i;
	}
	
	public synchronized Integer get() {
		while (list.size() == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		notifyAll();
		return list.remove(0);
	}
	
	public synchronized int getCount() {
		return list.size();
	}
	
	public static void main(String[] args) {
		T39_SynchronizedContainer c = new T39_SynchronizedContainer(5);
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
