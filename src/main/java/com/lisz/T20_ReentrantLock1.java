package com.lisz;

import java.util.concurrent.TimeUnit;

/*
 * synchronized就是可重入锁的一种。可重入的意思就是锁过之后，对同样一个对象可以再锁一次
 * 如果不能重入那在子类的m()方法里不能调用父类的synchronized super.m()方法。如果是
 * synchronized(this)父类和子类就是同一把锁，因为是同一个this
 */
public class T20_ReentrantLock1 {

	synchronized void m1() {
		for (int i = 0; i < 10; i++) {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(i);
			if (i == 2) {
				m2(); //m2可以在m1中被调用，无需等待，直接重入
			}
		}
	}
	
	synchronized void m2() {
		System.out.println("m2 ...");
	}
	
	public static void main(String[] args) {
		T20_ReentrantLock1 t = new T20_ReentrantLock1();
		new Thread(t::m1).start();
	}

}
