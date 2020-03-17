package com.lisz;

import java.util.ArrayList;
import java.util.List;

public class T39_SynchronizedContainer {
	private int capacity;
	private List<Integer> list = new ArrayList<>();
	
	public T39_SynchronizedContainer(int capacity) {
		super();
		this.capacity = capacity;
	}
	
	
	public synchronized void put(int i) {
		if (list.size() == capacity) {
			return;
		}
		list.add(i);
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
