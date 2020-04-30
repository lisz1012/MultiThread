package com.lisz.lock;
/* 线程状态不是重要的知识点，了解一下对操作系统有好处
 * 调用start之后会被系统线程调度器执行，状态整个是Runnable，而Runnable内部又有两个状态：
 * Ready 和 Running。Ready表示线程已经被扔到CPU的等待队列里面去了，在等待队列里面排着队
 * 等着让CPU运行；真正的在CPU上运行的状态叫Running。Thread.yield()的时候，会从Running
 * 切换到Ready；线程被选中执行的时候，又会被从Ready状态切换到Running。顺利地执行完线程之后
 * 线程进入Terminated状态，需要注意的是：一旦进入terminated之后就再也不能调用start从而
 * 进入NEW状态了。另外在Runnable中还有三个小状态：1.TimedWaiting 2. Waiting 3. Blocked
 * 等待进入同步代码块，还没有得到锁的时候会进入Blocked状态，一旦获得锁，则会切换到就绪Ready
 * 状态去运行。如果运行时调用了o.wait();t.join();LockSupport.park();则进入Waiting状态
 * o.notify(All());LockSupport.unpark();之后进入Runnable状态。sleep(time),wait(time)
 * ,join(time); LocakSupport.parkNanos()/parkUtil()会进入TimedWaiting状态，时间
 * 一结束就回去了.这些全是JVM管理的，因为JVM管理的时候也要通过操作系统，JVM本质来讲也是跑在
 * 操作系统上的一个普通程序。CPU挂起线程就是把执行中的线程扔回队列，杀死线程就会进入Terminated
 * 这里的线程跟操作系统的线程是一一对应的吗？要看JVM的实现，原来是，现在是不是不好说，最起码
 * 纤程跟OS线程不是一一对应的。线程应该一一对应。不要去关闭（stop）线程，让他正常结束。
 * 
 * sleep wait join的时候可能被打断，这时候得catch这个异常，自己的程序要写处理异常的代码，
 * 可以忽略继续运行，也可以停止。自己的工程里基本没有用interrupt控制业务逻辑的，netty框架
 * 深层用过，JDK的锁的源码里，为了程序健壮起见用到过。interrupt一般是这时候用：一个不停的，
 * 阻塞的或者sleep超长时间的线程，程序突然想全部关闭，此时调用interrrupt然后在sleep catch
 * 那个exception之后作出处理
 */
public class T03_Thread_State {
	public static class MyThread extends Thread {
		@Override
		public void run() {
			System.out.println(getState()); // RUNNABLE
			for (int i = 0; i < 10; i++) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(i);
			}
		}
	}
	
	public static void main(String[] args) {
		Thread t = new MyThread();
		System.out.println(t.getState()); // NEW
		t.start();
		try {
			t.join(); // 此时的小状态时Waiting，等待t线程执行结束之后再往下运行
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(t.getState()); // TERMINATED
	}

}
