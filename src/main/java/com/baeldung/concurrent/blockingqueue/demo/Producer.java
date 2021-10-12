package com.baeldung.concurrent.blockingqueue.demo;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {

    private BlockingQueue<Integer> queue;

    public Producer (BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    public void run() {

        try {

            for (int i = 0; i < 50; i++) {

                Integer number = produce();

                queue.put(number);

                System.out.println("PRODUCER: created " + number);

            }

            queue.put(-1);       // indicates end of producing

            System.out.println("PRODUCER: STOPPED.");

        } catch (InterruptedException ie) {

            ie.printStackTrace();

        }
    }

    private Integer produce() {
        Random random = new Random();

        Integer number = random.nextInt(100);

        // fake producing time
        try {
            Thread.sleep(random.nextInt(1000));
        } catch (InterruptedException ie) { ie.printStackTrace(); }


        return number;
    }
}
