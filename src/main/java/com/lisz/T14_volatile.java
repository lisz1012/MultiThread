package com.lisz;

import java.util.concurrent.TimeUnit;
/*
 * volatile 有两个作用：1.保证线程可见系性 2.禁止指令重排序
 * 除了java的heap这个个线程的共享内存之外，每个线程还有自己的工作内存，当线程之间有共享内存变量的时候，
 * 会把这个变量copy一份到各自的工作内存空间，这个空间有可能是CPU缓存，这样会比较快。对这个值的任何改变，
 * 都是改变在当前线程的工作内存里的变量的值，虽然他会马上写回共享空间，但是另一个线程什么时候检查这个共
 * 享空间的新值，不好控制。对副本进行的更改，并不能立刻反应到另外一个线程里，这叫线程之间不可见。加了
 * volatile之后，另一个线程就可以立刻见到。MESI：CPU的缓存一致性协议，多个CPU之间也需要进行缓存，由于
 * 不同的线程运行在不同的CPU上，一个CPU的缓存改了，其他CPU不一定马上能看得到。Java线程的可见性本质上
 * 要靠MESI来保证，必须有这个协议，两个缓存之间的同步的时候又有人改了怎么办？归根结底要靠硬件帮我们实现
 * 了。加了volatile之后，每次写都能被线程读到
 * 指令重排序：CPU在执行指令的时候一步一步的顺序执行，但是现在的CPU为了提高效率，它会让指令并发地被执行
 * 就是一个指令执行到一半的时候，第二个指令就已经开始执行了
 */
public class T14_volatile {
	volatile boolean running = true;
	void m() {
		System.out.println("m starts");
		while (running) {
			/*try {
				TimeUnit.MICROSECONDS.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
		}
		System.out.println("m ends");
	}
	
	public static void main(String[] args) {
		T14_volatile t = new T14_volatile();
		new Thread(t::m, "t1").start();
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		t.running = false;
	}
}
