package com.lisz.lock;

import java.util.concurrent.TimeUnit;

// 被锁定对象的属性发生改变不影响锁定，但是指针指向变成其他的对象则会影响锁定。可以用final修饰被锁定对象
public class T17_synchronized_object_pointer_changed {
	/*final */Object o = new Object();
	
	void m() {
		synchronized (o) {
			while (true) {
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(Thread.currentThread().getName());
			}
		}
	}
	
	
	public static void main(String[] args) {
		T17_synchronized_object_pointer_changed t = new T17_synchronized_object_pointer_changed();
		new Thread(t::m, "t1").start();
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		t.o = new Object();
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		new Thread(t::m, "t2").start();
	}

}
