package com.lisz;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/*
 * 读写锁的概念就是共享锁（读锁）和排他锁（写锁）
 * 读的特别多的场景：公司组织结构、电商品类结构。不想读到改到一半的数据，就得加锁，但是读线程特别多的时候效率会很低
 * 现在可以让一个读线程加一把锁，他读的时候可以允许其他的读线程来读，但是不允许写线程来写，写线程就会全部锁定。
 * 注：以后还是写synchronized，不写这些新玩意儿，除非特别追求效率的时候，无论什么，上来先想synchronized
 * 新玩意儿面试是第一位的. 秒杀就是多个线程加一个数，如0-500，这时候必须加锁，单机就是单机的锁，分布式就用分布式锁
 */
public class T30_ReadWriteLock {
	static Lock lock = new ReentrantLock();
	private static int value;
	
	static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	static Lock readLock = readWriteLock.readLock();
	static Lock writeLock = readWriteLock.writeLock();
	
	public static void read(Lock lock) {
		try {
			lock.lock();
			TimeUnit.SECONDS.sleep(1);
			System.out.println("Read over, value = " + value);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	public static void write(Lock lock, int v) {
		try {
			lock.lock();
			TimeUnit.SECONDS.sleep(1);
			value = v;
			System.out.println("Write over");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	public static void main(String[] args) {
		// 一个一个读
		//Runnable readR = ()->read(lock);
		//Runnable writeR = ()->write(lock, new Random().nextInt());
		
		// 18个读线程一起读，一秒钟完事儿
		Runnable readR = ()->read(readLock);
		Runnable writeR = ()->write(writeLock, new Random().nextInt());
		
		for (int i = 0; i < 18; i++) {
			new Thread(readR).start();
		}
		for (int i = 0; i < 2; i++) {
			new Thread(writeR).start();
		}
	}

}
