package com.lisz;

import java.util.concurrent.TimeUnit;

/*
 * synchronized就是可重入锁的一种。可重入的意思就是锁过之后，对同样一个对象可以再锁一次
 * 如果不能重入那在子类的m()方法里不能调用父类的synchronized super.m()方法。如果是
 * synchronized(this)父类和子类就是同一把锁，因为是同一个this
 * 
 * ReentrantLock用到了AQS的state，0代表没获得锁，1代表获得了锁，分别对应解锁和上锁。
 * state的值根据子类不同的实现取不同的意义。state之下维护着一个队列，里面有Node，类
 * 定义在AQS内部，Node里面有个Thread类型的成员，所以这个队列是一个线程队列，此队列使用
 * 一个双向链表实现。AQS的核心是一个state以及监控这个state的双向链表，每个节点里面装着
 * 线程，那个线程得到了锁或者要等待，都要进入这个队列里面，哪个node改state成功了，就说明
 * 他的这个线程拿到了这把锁。acquire的时候，看见state是0就直接拿到这把锁。
 * 
 * 读源码的时候画两种图：1.方法之间的调用图，还可以记一下每个方法都干了些什么事 2.类之间
 * 的类图 
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
