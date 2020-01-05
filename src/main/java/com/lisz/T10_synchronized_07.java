package com.lisz;

import java.util.concurrent.TimeUnit;

// 写方法加锁但是读方法不加锁，看业务允许不允许脏读，不允许的话给读方法也加上synchronized
// 看业务，能不加锁就不加锁，加完锁之后的效率低100倍
public class T10_synchronized_07 {

	private static final class Account {
		String name;
		double balance;
	
		public synchronized void set(String name, double balance) {
			this.name = name;
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.balance = balance;
		}
		
		public /*synchronized*/ double getBalance() {
			return balance;
		}
	}
	
	public static void main(String[] args) {
		Account a = new Account();
		new Thread(()->{a.set("zhangsan", 100.0);}).start();
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		new Thread(()->{System.out.println(a.getBalance());}).start();
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		new Thread(()->{System.out.println(a.getBalance());}).start();
	}

}
