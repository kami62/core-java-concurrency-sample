package demo.kamiah.waitnotify;

import java.util.concurrent.*;

public class WithBlockingQueue implements Callable<Void> {

    private static final int count = Integer.MAX_VALUE >>> 10;

    final int threadCount;

    public WithBlockingQueue(int threadCount) {
        this.threadCount = threadCount;
    }

    @Override
    public Void call() throws Exception {
        System.out.println("With " + this.getClass().getSimpleName());

        final Consumer consumer = new Consumer();
        final Producer producer = new Producer();

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

    private static final int sizeLimit = 1000;
    private static final BlockingQueue<Integer> list = new ArrayBlockingQueue<>(sizeLimit);

    static class Consumer {
        int consume() {
            try {
                return list.poll(1000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
    }


    static class Producer {
        void produce(int input) {
            try {
                list.offer(input, 1000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
    }
}
