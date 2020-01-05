package com.lisz;
// 同步方法和非同步方法能不能同时调用
public class T09_synchronized_06 {

	public synchronized void m1() {
		System.out.println(Thread.currentThread().getName() + " m1 starts");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + " m1 ends");
	}
	
	public void m2() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + " m2");
	}
	
	public static void main(String[] args) {
		T09_synchronized_06 t = new T09_synchronized_06();
		new Thread(t::m1, "t1").start();
		new Thread(t::m2, "t2").start();
	}

}
