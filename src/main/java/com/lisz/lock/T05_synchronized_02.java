package com.lisz.lock;

public class T05_synchronized_02 {
	private int count = 10;
	
	public void m() {
		synchronized (this) { //任何线程必须要拿到当前T05_synchronized_02对象的锁才可以执行里面的代码，不用一个专门的对象锁定
			count --;
			System.out.println(Thread.currentThread().getName() + " count = " + count);
		}
	}
}
