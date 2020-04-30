package com.lisz.lock;

public class T07_synchronized_04 {
	private static int count = 10;
	
	// 类文件加载到内存之后会专门生成一个Class类的对象，跟load到内存的这段代码相对应
	// 一个class文件load到内存之后生成的Class类型的对象一般情况下是单例的。如果是在同一个ClassLoader的空间里一定是单例的，否则可能不是单例，
	// 但是不同的加载器的类互相不能访问，所以不单例也就没什么关系了
	public synchronized static void m() { //static，此时还没有this对象生成，相当于synchronized(T07_synchronized_04.class)
		count --;
		System.out.println(Thread.currentThread().getName() + " count = " + count);
	}
	
	public static void mm() {
		synchronized (T07_synchronized_04.class) {
			count --;
		}
	}
}
