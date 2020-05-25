package com.lisz.disruptor;

import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.EventTranslatorVararg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

// https://github.com/LMAX-Exchange/disruptor/wiki/Getting-Started
public class Main03 {
    public static void main(String[] args) {
        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(LongEvent::new, 1024, DaemonThreadFactory.INSTANCE);
        disruptor.handleEventsWith((LongEvent event, long sequence, boolean endOfBatch) -> {
            System.out.println("Event: " + event);
        });
        disruptor.start();
        RingBuffer<LongEvent> buf = disruptor.getRingBuffer();

        buf.publishEvent((event, sequence, objects)->{
            Long sum = 0L;
            for (Object o : objects) {
                sum += (Long)o;
            }
            event.setValue(sum);
        });

        buf.publishEvent((event, sequence, l)->event.setValue(l), 1000L);
        buf.publishEvent((event, sequence, l1, l2)->event.setValue(l1 + l2), 1000L, 2000L);
    }
}
