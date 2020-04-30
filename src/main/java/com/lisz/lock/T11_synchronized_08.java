package com.lisz.lock;

import java.util.concurrent.TimeUnit;

// 一个同步方法可以调用另一个同步方法，一个线程已经拥有某个对象的锁，再次申请的时候
// 仍然会得到该对象的锁，可重入锁。同一个锁用被同一个线程申请，必须可重入，因为有个
// 父类，有个m方法，它的子类的m方法调用了super.m()，如果m是synchronized，此时
// 如果不能重入的话就会直接死锁了。锁是子类对象头上的mark word，子类里也有个父类对象
// 锁是同一个对象的锁
public class T11_synchronized_08 {

	public synchronized void m1() {
		System.out.println("m1 starts");
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		m2();
		System.out.println("m1 ends");
	}
	
	private synchronized void m2() {
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("m2");
	}

	public static void main(String[] args) {
		T11_synchronized_08 t = new T11_synchronized_08();
		t.m1();
	}

}
