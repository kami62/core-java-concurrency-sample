package com.baeldung.concurrent.blockingqueue;

public class MyBlockingQueueUsage {
    public static void main(String[] args) throws InterruptedException {
        int MAX = 5;

        System.out.println(" ------------------------ ");
        MyBlockingQueue<String> queue = new MyBlockingQueue<>(MAX);

        final Runnable producer = () -> {
            while (true){
                try {
                    System.out.println(" ----------ENQUEUE---------- ");
                    queue.put("A");
                    System.out.println(" ----------ENQUEUE---------- ");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(producer).start();
        new Thread(producer).start();


        final Runnable cosumer = () -> {
            while (true){
                try {
                    System.out.println(" ----------DEQUEUE---------- ");
                    String i = queue.take();
                    System.out.println(i);
                    System.out.println(" ----------DEQUEUE---------- ");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(cosumer).start();
        new Thread(cosumer).start();

        Thread.sleep(1000);
    }
}