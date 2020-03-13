package com.lisz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
 * 记住结论就好
 * 1.除了面试，volatile没有把握不要用
 * 2.volatile尽量修饰primitive的类型，别修饰引用变量
 */
public class T34_WithVolatile {

	volatile List lists = Collections.synchronizedList(new ArrayList<>());

	public void add(Object o) {
		lists.add(o);
	}

	public int size() {
		return lists.size();
	}
	
	public static void main(String[] args) {
		T34_WithVolatile c = new T34_WithVolatile();

		new Thread(() -> {
			for(int i=0; i<10; i++) {
				c.add(new Object());
				System.out.println("add " + i);
				
				try { //这里睡不睡都能得到想要的结果，但是在马士兵的机器上，不睡的话就出上个例子中的问题
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
