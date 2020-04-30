package com.lisz.lock;

import java.util.concurrent.locks.LockSupport;

/**
 * 两个线程，分别打印A-Z和1-26，但是要求交叉轮番打印：A1B2...Z26
 * @author shuzheng, 一次运行通过！！！哈哈哈
 *
 */
public class T41_AtoZ1to26Problem2 {
	private static Thread t1;
	private static Thread t2;
	
	public static void main(String[] args) {
		t1 = new Thread(()->{
			char c = 'A';
			for (int i = 0; i < 26; i++) {
				System.out.print((char)(c + i));
				LockSupport.unpark(t2);
				LockSupport.park();
			}
			LockSupport.unpark(t2); //t2打印完了26之后就park了，这里要unpark它一下
		}, "t1");
		
		t2 = new Thread(()->{
			LockSupport.park();
			for (int i = 0; i < 26; i++) {
				System.out.print(i+1);
				LockSupport.unpark(t1);
				LockSupport.park();
			}
		}, "t2");
		
		t1.start();
		t2.start();
	}
}
