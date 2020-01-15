package com.lisz;

import java.util.ArrayList;
import java.util.List;
/*
 * volatile 保证可见性，但是并不保证原子性。第一个线程从0加到1，第二个线程和第三个线程都能看见，
 * 它们如果同时读到了1各自拿去做计算，则还是会有问题。count++不是原子性的操作，在java内部会分成
 * 好多条指令，至少三条。所以volatile不能替代synchronized。解决：在m方法前面加synchronized
 */
public class T15_volatile_02 {
	volatile int count = 0;
	/*synchronized*/ void m() {for(int i=0; i<10000; i++) count++;}
	
	public static void main(String[] args) {
		T15_volatile_02 t = new T15_volatile_02();
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
