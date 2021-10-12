package com.baeldung.concurrent.blockingqueue;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class MyBlockingQueue<E> {

    private int max;
    private Queue<E> queue;

    private ReentrantLock lock = new ReentrantLock(true);

    private Object notEmpty = new Object();
    private Object notFull = new Object();

    public MyBlockingQueue(int max) {
        queue = new LinkedList<>();
        this.max = max;
    }

    public synchronized void put(E e) throws InterruptedException {
        while (queue.size() == max){
            notFull.wait();
        }

        queue.add(e);
        notEmpty.notifyAll();

    }

    public synchronized E take() throws InterruptedException {
        if(queue.size() == 0){
            notEmpty.wait();
        }
        E item = queue.remove();

        System.out.println(item);

        notFull.notifyAll();
        return item;
    }
}
