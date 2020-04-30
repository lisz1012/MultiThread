package com.lisz.lock;

/* CPU很傻，就是一个死循环的执行内存中拿过来的一条条的指令，没有指令了就歇着。
 * 多线程就是每个线程在CPU（单个CPU的情况）上面执行一会儿。sleep就是当前线程
 * 停止一定的时间，让给别的线程去运行，这段时间内不参与跟别的线程争抢CPU资源.
 * sleep完了回到就绪状态
 * 
 * yield执行示例里面可以看到：每到被10整除的时候总是被打断，切换线程。非常谦让
 * 地退出一下，但是让出多长时间，不确定。所谓的退出，其实就是进入一个等待队列。
 * 在OS的算法里还是用可能刚回到队列里又拿出来继续执行，但是更可能的是把原来在等
 * 待的拿出来一个执行。yield是让出一下CPU，至于后边谁能抢到，我不管。返回到
 * 就绪状态去一下，这种使用场景很少，只有压测的时候有可能用到。
 * 
 * join经常用来等待另外一个线程的结束。在t2线程运行的时候，在其run方法中调用
 * t1.join()，这样会让t2停下来，t1开始执行，等t1执行完了t2再继续执行.如何
 * 让三个线程依次执行完毕？在主线程里调用t1.join(),t1里面调用t2.join()...
 * 线程在自己的run方法里面调用自己的join是没有意义的
 */
public class T02_Sleep_Yield_Join {

	public static void main(String[] args) {
		//testSleep();
		//testYield();
		testJoin();
	}

	public static void testSleep() {
		new Thread(() -> {
			for (int i = 0; i < 100; i++) {
				System.out.println("A" + i);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}) .start();
	}
	
	public static void testYield() {
		new Thread(() -> {
			for (int i = 0; i < 100; i++) {
				System.out.println("A" + i);
				if (i % 10 == 0) {
					Thread.yield();
				}
			}
		}).start();
		
		new Thread(() -> {
			for (int i = 0; i < 100; i++) {
				System.out.println("--------B" + i);
				if (i % 10 == 0) {
					Thread.yield();
				}
			}
		}).start();
	}
	
	public static void testJoin() {
		Thread t1 = new Thread(() -> {
			for (int i = 0; i < 10; i++) {
				System.out.println("A" + i);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		Thread t2 = new Thread(()->{
			System.out.println("t2 starts...");
			try {
				t1.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("t2 ends");
		});
		
		t1.start();
		t2.start();
	}
}
