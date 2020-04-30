package com.lisz.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
 * 曾经的面试题：
 * 实现一个容器，提供两个方法：add和size
 * 写两个线程，线程1添加10个元素到容器中，线程2实现监控元素的个数，当个数到达5，线程2给出提示并结束
 * 下面的程序能完成功能吗？
 * 不行。因为： 1. ArrayList内部加元素和size++没有同步 2. c.size() == 5永远没有检测到，线程间不可见
 * size的值第一个线程变了，第二个线程不会马上看见
 */
public class T34_WithoutVolatile {

	/*volatile*/ List lists = new ArrayList();

	public void add(Object o) {
		lists.add(o);
	}

	public int size() {
		return lists.size();
	}
	
	public static void main(String[] args) {
		T34_WithoutVolatile c = new T34_WithoutVolatile();

		new Thread(() -> {
			for(int i=0; i<10; i++) {
				c.add(new Object());
				System.out.println("add " + i);
				
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "t1").start();
		
		new Thread(() -> {
			while(true) {
				//System.out.println("Current size: " + c.size());
				if(c.size() == 5) {
					break;
				}
			}
			System.out.println("t2 结束");
		}, "t2").start();
	}

}
