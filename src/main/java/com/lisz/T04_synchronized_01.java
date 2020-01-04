package com.lisz;
/*
 * synchronized的底层实现在JVM里面没有任何要求，反正最后结果。反正就是效果是加了这把锁就能执行这段代码
 * 就OK了。Hotspot是在堆里面的对象的头上面拿出两位（mark word，这两位的01组合表示不同锁的类型）来记录这
 * 个对象是不是被锁定了。
 */
public class T04_synchronized_01 {

	private int count = 10;
	private Object o = new Object();
	
	public void m() {
		synchronized (o) { //想锁谁就锁谁，但是任何线程必须要拿到o的锁才可以执行里面的代码
			count --;
			System.out.println(Thread.currentThread().getName() + " count = " + count);
		}
	}

}
