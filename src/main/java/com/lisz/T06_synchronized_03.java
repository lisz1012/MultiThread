package com.lisz;

public class T06_synchronized_03 {
	private int count = 10;
	
	//任何线程必须要拿到当前T05_synchronized_02对象的锁才可以执行里面的代码，不用一个专门的对象锁定
	//相当于synchronized(this)...
	public synchronized void m() {
		count --;
		System.out.println(Thread.currentThread().getName() + " count = " + count);
	}
}
