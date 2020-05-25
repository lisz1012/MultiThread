package com.lisz.disruptor;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main07_ExceptionHandler {
    public static void main(String[] args) {
        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(LongEvent::new, 1024, Executors.defaultThreadFactory(), ProducerType.MULTI, new SleepingWaitStrategy());
        EventHandler<LongEvent> h1 = (event, sequence, end)->{
            System.out.println(event);
            throw new Exception("消费者出异常");
        } ;
        disruptor.handleEventsWith(h1);
        disruptor.setDefaultExceptionHandler(new ExceptionHandler<LongEvent>() {
            @Override
            public void handleEventException(Throwable ex, long sequence, LongEvent event) {
                ex.printStackTrace();
            }

            @Override
            public void handleOnStartException(Throwable ex) {
                System.out.println("ExceptionHandler starts to handle");
            }

            @Override
            public void handleOnShutdownException(Throwable ex) {
                System.out.println("Exception handled!");
            }
        });
        disruptor.start();
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        int threadCount = 1;
        CyclicBarrier barrier = new CyclicBarrier(threadCount);
        ExecutorService service = Executors.newCachedThreadPool();
        for (int i = 0; i < threadCount; i++) {
            final int threadNum = i;
            service.submit(()->{
                System.out.println("Thread " + threadNum + " is ready to start!");
                try {
                    barrier.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            for (int j = 0; j < 1; j++) {
                ringBuffer.publishEvent((event, sequence)->{
                    event.setValue(threadNum);
                    System.out.println("生产了 " + threadNum);
                });
            }
        }

        service.shutdown();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(LongEventHandler.count);
    }
}
