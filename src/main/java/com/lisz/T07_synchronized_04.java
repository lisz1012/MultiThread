package com.lisz;

public class T07_synchronized_04 {
	private static int count = 10;
	
	// 类文件加载到内存之后会专门生成一个Class类的对象
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
