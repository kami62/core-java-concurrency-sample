package com.baeldung.concurrent.blockingqueue.linked;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LinkedBlockingQueueTest {

    public static void main(String[] args) {

        BlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>(5);

        Thread producer = new Thread(new Producer(queue));

        Thread consumer = new Thread(new Consumer(queue));

        producer.start();

        consumer.start();

    }
}
