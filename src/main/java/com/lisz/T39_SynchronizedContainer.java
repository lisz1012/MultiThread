package com.lisz;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class T39_SynchronizedContainer {
	private int capacity;
	private List<Integer> list = new ArrayList<>();
	
	public T39_SynchronizedContainer(int capacity) {
		super();
		this.capacity = capacity;
	}
	
	
	public synchronized Integer put(int i) {
		if (list.size() == capacity) {
			return null;
		}
		list.add(i);
		return i;
	}
	
	public synchronized Integer get() {
		if (list.size() == 0) {
			return null;
		}
		return list.remove(0);
	}
	
	public synchronized int getCount() {
		return list.size();
	}
	
	public static void main(String[] args) {
		T39_SynchronizedContainer c = new T39_SynchronizedContainer(5);
		Random r = new Random();
		for (int i = 0; i < 2; i++) {
			new Thread(()->{
				for (int j = 0; j < 10; j++) {
					System.out.println("Added " + c.put(j));
					try {
						Thread.sleep(r.nextInt(1000));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
		
		for (int i = 0; i < 10; i++) {
			new Thread(()->{
				System.out.println("Get " + c.get());
				try {
					Thread.sleep(r.nextInt(1000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}).start();
		}
		
		for (int i = 0; i < 3; i++) {
			new Thread(()->{
				System.out.println("Count: " + c.getCount());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}).start();
		}
	}

}
