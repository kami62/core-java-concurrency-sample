package com.baeldung.concurrent.blockingqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingQueueUsage {
    public static void main(String[] args) {
        int BOUND = 10;
        int N_PRODUCERS = 4;
        int N_CONSUMERS = Runtime.getRuntime().availableProcessors();
        System.out.println(" ------------------------ ");
        System.out.println("Runtime.getRuntime().availableProcessors() : " + Runtime.getRuntime().availableProcessors());
        int poisonPill = 32;
        System.out.println("poisonPill : " + poisonPill);
        int poisonPillPerProducer = N_CONSUMERS / N_PRODUCERS;
        System.out.println("poisonPillPerProducer : " + poisonPillPerProducer);
        int mod = N_CONSUMERS % N_PRODUCERS;
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(BOUND);

        for (int i = 1; i < N_PRODUCERS; i++) {
            new Thread(new NumbersProducer(queue, poisonPill, poisonPillPerProducer)).start();
        }

        for (int j = 0; j < N_CONSUMERS; j++) {
            new Thread(new NumbersConsumer(queue, poisonPill)).start();
        }
        
        new Thread(new NumbersProducer(queue, poisonPill, poisonPillPerProducer + mod)).start();

    }
}