package demo.kamiah.waitnotify;

public class Application {

    public static void main(String[] args) throws Exception {
        final int threadCount = Runtime.getRuntime().availableProcessors()/2;
        System.out.printf("Using %s threads\n", threadCount);

        final WaitNotifyQueue waitNotifyQueue = new WaitNotifyQueue(threadCount);
        waitNotifyQueue.call();

        System.gc();

        final WithBlockingQueue withBlockingQueue = new WithBlockingQueue(threadCount);
        withBlockingQueue.call();

        System.gc();

        final WaitNotifyLockQueue waitNotifyLockQueue = new WaitNotifyLockQueue(threadCount);
        waitNotifyLockQueue.call();
    }
}
