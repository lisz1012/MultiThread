package com.lisz.container;

import java.util.concurrent.LinkedTransferQueue;

public class T10_TransferQueue {
    public static void main(String[] args) throws InterruptedException {
        LinkedTransferQueue<String> strs = new LinkedTransferQueue<>();
        new Thread(()->{
            try {
                System.out.println(strs.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        strs.transfer("aaa"); //transfer和put不一样的地方在于：put装完了就走，transfer不走，等着有人把它取走才继续做他的事情去，队列不满也在那里等着.队列size不为0，别人可能offer来着
        // 可以等待着多个线程来消费
        /*new Thread(()->{
            try {
                System.out.println(strs.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();*/
    }
}
