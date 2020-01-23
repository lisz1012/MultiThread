package com.lisz;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

// 凡是以Atomic开头的，都是用CAS来保证线程安全的类
public class T18_AtomicInteger {
	AtomicInteger count = new AtomicInteger();
	void m() {
		for (int i = 0; i < 10000; i++) {
			count.incrementAndGet();//++count，这里马老师写的是count++，有点小错误哈
		}
	}
	public static void main(String[] args) {
		T18_AtomicInteger t = new T18_AtomicInteger();
		List<Thread> threads = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			threads.add(new Thread(t::m, "thread - " + i));
		}
		threads.forEach(o->o.start());
		threads.forEach(o->{
			try {
				o.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		System.out.println(t.count);
	}

}
