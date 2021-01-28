package edu.vt.ece.hw4.backoff;

import java.util.concurrent.atomic.AtomicInteger;

public class LinearBackoff implements Backoff {
    static AtomicInteger fail = new AtomicInteger(1);

    @Override
    public void backoff() throws InterruptedException {
        long delay = fail.getAndIncrement();
        if(delay > 1024) {
            delay = 1024;
        }
        System.out.println("Linear delay "+delay);
        Thread.sleep(delay);
    }
}
