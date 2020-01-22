package com.lisz;

import java.util.concurrent.TimeUnit;

// 两个synchronized锁的是同一个对象，所谓的重入锁就是自己拿到这把锁之后，
// 不停的加锁加锁再加锁，加好几道，然后都是自己这把锁，锁的是同一个对象。
// 每去一道锁就计数 -1，也就是说最里面的一个同步方法执行完，锁其实还没释放
// 要等最外层的方法执行完了才释放
public class T12_synchronized_09 {

	private static class Parent {
		public synchronized void m() {
			System.out.println("m starts");
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("m ends");
		}
	}
	
	private static class Child extends Parent {
		@Override
		public synchronized void m() {
			System.out.println("Child m starts");
			super.m();
			System.out.println("Child m ends");
		}
	}
	
	public static void main(String[] args) {
		new Child().m();
	}

}
