package com.lisz;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/*
 * 还是有问题，notify并不会释放锁，而wait完回来执行的时候必须还得拿到这把锁
 */
public class T38_LockSupport {

	volatile List lists = new ArrayList<>();

	public void add(Object o) {
		lists.add(o);
	}

	public int size() {
		return lists.size();
	}
	
	private static Thread t1 = null;
	private static Thread t2 = null;
	
	public static void main(String[] args) {
		T38_LockSupport c = new T38_LockSupport();
		//CountDownLatch latch = new CountDownLatch(1);
		

		t2 = new Thread(() -> {
			LockSupport.park();
			System.out.println("t2 结束");
			//latch.countDown();
			LockSupport.unpark(t1);
		}, "t2");
		
		t1 = new Thread(() -> {
			for(int i=0; i<10; i++) {
				c.add(new Object());
				System.out.println("add " + i);
				if (c.size() == 5) {
					LockSupport.unpark(t2);
					/*try {
						latch.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}*/
					LockSupport.park();
				}
				/*try { //注释掉之后必须用CountDownLatch.
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
			}
		}, "t1");
		
		t1.start();
		t2.start(); //最极端的情况是t1已经在50行处park了，t2还没开始，当t2开始执行，则直接回一直执行到最后，不park然后t1放开继续，结果一样不会出错
	}

}
