package com.lisz.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/*
 * unpark相当于一张通行证，线程相当于司机。一个司机最多拿一张通行证，一张通行证只能通过一个路卡（park）
 * 一旦遇到路卡，就检查通行证，没有就停车，有就通过并销毁通行证；停下来的司机不得通行，直到被颁发通行证。
 */
public class T33_TestLockSupport {

	public static void main(String[] args) {
		Thread t = new Thread(()->{
			for (int i = 0; i < 10; i++) {
				System.out.println(i);
				if (i == 5 || i == 6) {
					LockSupport.park();
				}
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		t.start();
		LockSupport.unpark(t); // 上来就颁发通行证，预期的第一个park也不让执行了，unpark可以先于park之前执行。而notify不能先于wait执行
		//LockSupport.unpark(t);
		
		try {
			TimeUnit.SECONDS.sleep(8);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("After 8 seconds!");
		// 这里可以叫醒指定的线程
		LockSupport.unpark(t);
	}

}
