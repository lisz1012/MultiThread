package com.lisz;

public class T01_Thread_Creation {

	private static class MyThread extends Thread {
		@Override
		public void run() {
			System.out.println("My Thread");
		}
	}
	
	private static class MyRunnable implements Runnable {
		public void run() {
			System.out.println("My Runnable");
		}
	}
	
	public static void main(String[] args) {
		new MyThread().start();
		new Thread(new MyRunnable()).start();//用Runnable接口的话需要在new Thread的时候传进去Runnable对象
		new Thread(() -> {
			System.out.println("My lambda");
		}).start();
	}

}
// 启动thread的3种方式：1.继承 2.Runnable接口 3.通过线程池来启动Executor.newCachedThread()（其实内部也是用的前两种）