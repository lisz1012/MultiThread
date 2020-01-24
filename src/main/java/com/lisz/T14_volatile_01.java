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
 * 就是一个指令执行到一半的时候，第二个指令就已经开始执行了, 流水线式的执行。这样会大幅度提高CPU效率如果
 * 想充分利用CPU的这个特点，就要求编译器对源码编译完的指令进行重新排序，细节上就是汇编语言上的重排序。
 * 单例模式的懒汉模（饿汉模型JVM初始化会保证只有一个实例）型就用volatile避免了初始化一半并且引用不为
 * null的对象返回。记得吗？懒汉模式里我们为了保证线程安全，会有两次判断，且synchronized夹在它们中间。
 * volatile还是要加的，因为new一个对象的时候，一条new语句编译成汇编之后分三步1.申请内存 2.给对象的
 * 成员变量初始化(先使用默认 0 false null，如果有用户赋值，则用真正的的值对他初始化)。 3.把内存的
 * 内容赋值给INSTANCE。但是如果有指令重排，会发生还没有初始化成员，却先返回了INSTANCE的引用，此时就会
 * 拿到初始化了一半的对象。在超高超高并发的情况下，比如阿里双十一秒杀等，会出现错误。加了volatile，对这
 * 个对象上的指令重排序就不存在了. 读写重排序用到了CPU原语：load fence（读的指令必须执行完才能执行后面的）
 * 和store fence ----- 读写屏障
 * 
 * 存储过程是运行在数据库里的，业务逻辑写在数据库内部，比在程序里处理效率快多了，计算向数据移动，存储过程
 * 中写一堆判断循环select比程序里效率高的多得多，上千倍。程序读数据库搞不好还不在一台机器上，中间有网络
 * 传输，太慢。传统公司很多都在用存储过程。MySQL深入的部分以后再说
 * 
 * 这些知识在工作有余力的前提下可以多研究一下.实际写程序，volatile基本用不着，这就是用来干面试的
 */
public class T14_volatile_01 {
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
		T14_volatile_01 t = new T14_volatile_01();
		new Thread(t::m, "t1").start();
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		t.running = false;
	}
}
