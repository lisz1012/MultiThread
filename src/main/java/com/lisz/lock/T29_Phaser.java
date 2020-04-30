package com.lisz.lock;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class T29_Phaser {
	static Random r = new Random();
	static MarriagePhaser phaser = new MarriagePhaser();
	
	static void milliSleep(int millis) {
		try {
			TimeUnit.MILLISECONDS.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		phaser.bulkRegister(7);
		for (int i = 0; i < 5; i++) {
			new Thread(new Person("person " + i)).start();
		}
		
		new Thread(new Person("新郎")).start();
		new Thread(new Person("新娘")).start();
	}
	
	static class MarriagePhaser extends Phaser {
		// 所有的线程都满足栅栏的条件的时候onAdvance会被自动调用
		@Override
		protected boolean onAdvance(int phase, int registeredParties) { //到哪个阶段了以及栅栏推倒之后还有多少人
			switch (phase) {
			case 0:
				System.out.println("所有人都到齐了!" + registeredParties);
				System.out.println();
				return false;
			case 1:
				System.out.println("所有人都吃完了!" + registeredParties);
				System.out.println();
				return false;
			case 2:
				System.out.println("所有人都离开了!" + registeredParties);
				System.out.println();
				return false;
			case 3:
				System.out.println("婚礼结束!新郎新娘抱抱！" + registeredParties);
				System.out.println();
				return true; //return true的时候所有的线程就结束了，栅栏组就结束了
			default:
				return true;
			}
		}
	}

	static class Person implements Runnable {
		String name;
		public Person(String name) {
			this.name = name;
		}

		public void leave() {
			milliSleep(r.nextInt(1000));
			System.out.println(name + "离开");
			phaser.arriveAndAwaitAdvance();
		}

		public void arrive() {
			milliSleep(r.nextInt(1000));
			System.out.println(name + "到达");
			phaser.arriveAndAwaitAdvance();
		}

		public void eat() {
			milliSleep(r.nextInt(1000));
			System.out.println(name + "吃");
			phaser.arriveAndAwaitAdvance();
		}

		private void hug() {
			if (name.equals("新郎") || name.equals("新娘")) {
				milliSleep(r.nextInt(1000));
				System.out.println(name + "洞房");
				phaser.arriveAndAwaitAdvance();
			} else {
				phaser.arriveAndDeregister();
				//phaser.register();会往上加一个线程
			}
		}
		
		@Override
		public void run() {
			arrive();
			eat();
			leave();
			hug();
		}
		
	}
}
