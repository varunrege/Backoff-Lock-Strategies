package edu.vt.ece.hw4.backoff;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Balaji Arun
 */
public class FibonacciBackoff implements Backoff {
    static AtomicInteger prev = new AtomicInteger(0);
    static AtomicInteger next = new AtomicInteger(1);
    @Override
    public void backoff() throws InterruptedException {
        long delay = prev.get() + next.get();
        prev.set(next.get());
        next.set((int)delay);
        if(delay > 1024) {
            delay = 1024;
        }
        System.out.println("Fibonacci delay "+delay);
        Thread.sleep(delay);
    }
}
