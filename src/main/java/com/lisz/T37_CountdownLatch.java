package com.lisz;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/*
 * 还是有问题，notify并不会释放锁，而wait完回来执行的时候必须还得拿到这把锁
 */
public class T37_CountdownLatch {

	volatile List lists = new ArrayList<>();

	public void add(Object o) {
		lists.add(o);
	}

	public int size() {
		return lists.size();
	}
	
	public static void main(String[] args) {
		T37_CountdownLatch c = new T37_CountdownLatch();
		CountDownLatch latch = new CountDownLatch(5);
		CountDownLatch latch2 = new CountDownLatch(1);
		
		new Thread(() -> {
			try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("t2 结束");
			latch2.countDown();
		}, "t2").start();
		
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		new Thread(() -> {
			for(int i=0; i<10; i++) {
				c.add(new Object());
				System.out.println("add " + i);
				latch.countDown();
				if (c.size() == 5) {
					try {
						latch2.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				/*try { //这里睡觉不能删，删了之后可能先执行输出“add 6”然后输出“t2 结束”
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
			}
		}, "t1").start();
		
		
	}

}
