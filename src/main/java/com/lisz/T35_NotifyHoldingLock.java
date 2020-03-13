package com.lisz;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
 * 还是有问题，notify并不会释放锁，而wait完回来执行的时候必须还得拿到这把锁
 */
public class T35_NotifyHoldingLock {

	volatile List lists = new ArrayList<>();

	public void add(Object o) {
		lists.add(o);
	}

	public int size() {
		return lists.size();
	}
	
	public static void main(String[] args) {
		T35_NotifyHoldingLock c = new T35_NotifyHoldingLock();
		Object lock = new Object();
		
		new Thread(() -> {
			synchronized (lock) {
				if (c.size() != 5) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("t2 结束");
			}
		}, "t2").start();
		
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		new Thread(() -> {
			synchronized (lock) {
				for(int i=0; i<10; i++) {
					c.add(new Object());
					System.out.println("add " + i);
					if (c.size() == 5) {
						lock.notify();
					}
					try { //这里睡不睡都能得到想要的结果，但是在马士兵的机器上，不睡的话就出上个例子中的问题
						TimeUnit.SECONDS.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}, "t1").start();
		
		
	}

}
