package demo.kamiah.waitnotify;

import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WaitNotifyQueue implements Callable {


    private static final int count = Integer.MAX_VALUE >>> 10;
    final int threadCount;

    public WaitNotifyQueue(int threadCount) {
        this.threadCount = threadCount;
    }

    @Override
    public Void call() throws Exception {
        System.out.println("With " + this.getClass().getSimpleName());

        final Consumer consumer = new Consumer();
        final Producer producer = new Producer();

        // 10 thread. 5 producers. 5 consumers
        final ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        final long startNano = System.nanoTime();
        for (int j = 0; j < threadCount / 2; j++) {
            executorService.submit(() -> {
                for (int i = 0; i < count; i++) {
                    final int consume = consumer.consume();
                }
            });
        }
        for (int j = 0; j < threadCount / 2; j++) {
            executorService.submit(() -> {
                for (int i = 0; i < count; i++) {
                    producer.produce(i);
                }
            });
        }

        executorService.shutdown();
        if (!executorService.awaitTermination(20, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }

        System.out.println("Task is done. Queue size = " + list.size());
        System.out.println("Time taken in second: " + (System.nanoTime() - startNano) * 1e-9);

        return null;
    }

    private static final LinkedList<Integer> list = new LinkedList<>();
    private static final int sizeLimit = 1000;

    class Consumer {
        int consume() {
            synchronized (list) {
                while (list.isEmpty() && !Thread.currentThread().isInterrupted()) {
                    try {
                        list.wait(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

                int ele = list.removeFirst();
                list.notify();

                return ele;
            }
        }
    }


    class Producer {
        void produce(int input) {

            synchronized (list) {
                while (list.size() == sizeLimit && !Thread.currentThread().isInterrupted()) {
                    try {
                        list.wait(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

                list.add(input);
                list.notify();
            }
        }
    }
}