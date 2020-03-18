package com.lisz;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/*
 * Semaphore叫信号灯，参数是一个数字：允许几个线程来参考信号灯。下面一定是一个执行完了才执行另一个线程
 * 跟CyclicBarrier正好相反，他是数到0就不让执行了；而CyclicBarrier是await的线程数增加到某个数才
 * 可以大家一起执行。Semaphore起到限流的作用，最多允许多少个线程并发执行。FixedThreadPool里面只有两个
 * 线程，Semaphore可以有一百个线程过来，但是同时运行的只有两个。跟线程池没有半毛钱关系。限流，类似于
 * 车道和收费站。跟前面的Barrier、Latch、Phaser一样都用了AQS（AbstractQueuedSynchronizer）队列。
 * AQS是高并发最核心的内容
 */
public class T31_Semaphore {

	public static void main(String[] args) {
		Semaphore s = new Semaphore(1);
		// Semaphore s = new Semaphore(1, true); //公平的概念是后面来的车肯定不会抄近道加塞。不公平效率比较高
		new Thread(()->{
			try {
				//阻塞方法，一旦acquire到了，permits就减去1，当变成0之后别人就无法acquire了。要想继续往下执行，必须获得一个许可
				s.acquire();
				System.out.println("T1 running...");
				TimeUnit.SECONDS.sleep(1);
				System.out.println("T1 running...");
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				s.release(); //permits加回去
			}
			
		}).start();
		
		new Thread(()->{
			try {
				s.acquire();
				System.out.println("T2 running...");
				TimeUnit.SECONDS.sleep(1);
				System.out.println("T2 running...");
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				s.release(); //permits加回去
			}
			
		}).start();
	}

}
