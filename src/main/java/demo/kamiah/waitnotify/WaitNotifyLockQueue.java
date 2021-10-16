package demo.kamiah.waitnotify;

import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class WaitNotifyLockQueue implements Callable<Void> {

    private static final int count = Integer.MAX_VALUE >>> 10;
    final int threadCount;

    private static final LinkedList<Integer> list = new LinkedList<>();
    private static final int sizeLimit = 1000;
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition notFullSignal = lock.newCondition();
    private static final Condition notEmptySignal = lock.newCondition();
    static class Consumer {
        int consume() {
            lock.lock();
            try {
                while (list.isEmpty() && !Thread.currentThread().isInterrupted()) {
                    try {
                        // wait for notEmpty signal
                        notEmptySignal.await(1000, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                int ele = list.removeFirst();
                // send notFull signal
                notFullSignal.signal();
                return ele;
            } finally {
                lock.unlock();
            }
        }
    }

    static class Producer {
        void produce(int input) {

            lock.lock();
            try {
                while (list.size() == sizeLimit && !Thread.currentThread().isInterrupted()) {
                    try {
                        // wait for notFull signal
                        notFullSignal.await(1000, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                list.add(input);
                // send notEmpty signal
                notEmptySignal.signal();
            } finally {
                lock.unlock();
            }
        }
    }

    public WaitNotifyLockQueue(int threadCount) {
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


}
