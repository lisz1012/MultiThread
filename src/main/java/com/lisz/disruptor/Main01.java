package com.lisz.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.concurrent.Executors;
// https://github.com/LMAX-Exchange/disruptor/wiki/Getting-Started
public class Main01 {
    public static void main(String[] args) {
        LongEventFactory factory = new LongEventFactory();
        int bufferSize = 1024;
        //调用消费者的时候是在一个线程中执行的，这个线程是怎么产生的。在这个线程中把event拿出来调用LongEventHandler的onEvent
        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(factory, bufferSize, Executors.defaultThreadFactory());
        disruptor.handleEventsWith(new LongEventHandler());
        disruptor.start();
        RingBuffer<LongEvent> buf = disruptor.getRingBuffer();
        long sequence = buf.next();
        LongEvent event = buf.get(sequence);
        event.setValue(8888L);
        buf.publish(sequence);
    }
}
