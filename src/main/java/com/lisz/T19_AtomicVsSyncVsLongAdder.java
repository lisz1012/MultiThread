package com.lisz;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
/*
下面结果看出最快的是LongAdder，但是线程数或者循环数比较小，LongAdder也没什么优势.LongAdder快的原因是，它的内部做了一个类似分段锁。由于加的值特别高，所以LongAdder会把数字放在一个数组里，
比如1000个线程，数组长度为4，每个元素都是0，则每个250个线程锁在一个元素上，每个都做递增，最后把4个结果加起来。相当于分片负载均衡。并发高、线程多的时候，LongAdder就体现出优势来了，反之，线
程数比较少的话，LongAdder没什么优势。LngAdder的分段锁里面也是CAS操作
顺便说一下，synchronized放在for的外面反而会快很多，比LongAdder还快，所以锁的申请很耗时
 */
public class T19_AtomicVsSyncVsLongAdder {
	static long count2;
	static AtomicLong count1 = new AtomicLong(0L);
	static LongAdder count3 = new LongAdder();
	static final int NUM_THREADS = 1000;
	static final int TIMES = 100000;
	
	public static void main(String[] args) throws Exception {
		Thread threads[] = new Thread[NUM_THREADS];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(()->{
				for (int j = 0; j < TIMES; j++) {
					count1.incrementAndGet();//getAndIncrement();
				}
			});
		}
		long start = System.currentTimeMillis();
		for (Thread t : threads) {
			t.start();
		}
		for (Thread t : threads) {
			t.join();
		}
		System.out.println("AtomicLong: " + (System.currentTimeMillis() - start));
		
		Object o = new Object();
		threads = new Thread[NUM_THREADS];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(()->{
				for (int j = 0; j < TIMES; j++) {
					synchronized (o) { 
						count2++;
					}
				}
			});
		}
		start = System.currentTimeMillis();
		for (Thread t : threads) {
			t.start();
		}
		for (Thread t : threads) {
			t.join();
		}
		System.out.println("synchronized: " + (System.currentTimeMillis() - start));
		
		threads = new Thread[NUM_THREADS];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(()->{
				for (int j = 0; j < TIMES; j++) {
					count3.increment();
				}
			});
		}
		start = System.currentTimeMillis();
		for (Thread t : threads) {
			t.start();
		}
		for (Thread t : threads) {
			t.join();
		}
		System.out.println("LongAdder: " + (System.currentTimeMillis() - start));
	}

}
/*
AtomicLong: 2052
synchronized: 1934
LongAdder: 315
最快的是LongAdder，但是线程数或者循环数比较小，LongAdder也没什么优势.LongAdder快的原因是，它的内部做了一个类似分段锁。由于加的值特别高，所以LongAdder会把数字放在一个数组里，
比如1000个线程，数组长度为4，每个元素都是0，则每个250个线程锁在一个元素上，每个都做递增，最后把4个结果加起来。相当于分片负载均衡
250个线程
*/