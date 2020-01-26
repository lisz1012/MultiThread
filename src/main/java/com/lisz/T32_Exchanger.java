package com.lisz;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
/*
 * 了解就行。
 * 奇数个线程exchange的话，多出来的那个线程就得等着了，有点CyclicBarrier的概念。
 * Exchanger想象成一个容器，有两个格子。第一个线程执行到exchange方法的时候，会把自己的一个值传进去，
 * 放到容器的一个格子里。因为exchange是阻塞的，于是会停下来等待其他的线程也执行到同一个exchanger的
 * exchange方法。当另一个线程也执行到这一语句的时候也会把他的某个值传进去，放在容器的另一个格子里。
 * 然后两个值交换一下，然后两个线程各自继续执行. 入门建议看《实战Java高并发程序设计》
 * 题外话：分段锁有LongAdder和ConcurrentHashMap等、Jstack可以排查到线程间的死锁
 */
public class T32_Exchanger {
	static Exchanger<String> exchanger = new Exchanger<>();
	
	public static void main(String[] args) {
		new Thread(()->{
			String s = "T1";
			try {
				s = exchanger.exchange(s);
				//s = exchanger.exchange(s, 1000, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread() + " " + s);
		}, "t1").start();
		
		new Thread(()->{
			String s = "T2";
			try {
				s = exchanger.exchange(s);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread() + " " + s);
		}, "t2").start();
		
		new Thread(()->{
			String s = "T3";
			try {
				s = exchanger.exchange(s);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread() + " " + s);
		}, "t3").start();
		
		new Thread(()->{
			String s = "T4";
			try {
				s = exchanger.exchange(s);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread() + " " + s);
		}, "t4").start();
	}

}
