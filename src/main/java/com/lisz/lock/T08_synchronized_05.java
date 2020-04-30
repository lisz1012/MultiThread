package com.lisz.lock;

public class T08_synchronized_05 implements Runnable {
	private /*volatile*/ int count = 10;

	@Override
	public synchronized void run() {
		count --;
		System.out.println(Thread.currentThread().getName() + " count = " + count);
	}
	
	public static void main(String[] args) {
		T08_synchronized_05 t = new T08_synchronized_05();
		for (int i = 0; i < 5; i++) {
			new Thread(t, "Thread" + i).start();
		}
	}
}
