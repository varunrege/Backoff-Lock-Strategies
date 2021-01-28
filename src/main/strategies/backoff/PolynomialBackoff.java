package edu.vt.ece.hw4.backoff;

import java.util.concurrent.atomic.AtomicInteger;

public class PolynomialBackoff implements Backoff {
    static AtomicInteger atmpt = new AtomicInteger(1);
    @Override
    public void backoff() throws InterruptedException {
        long delay = (long)Math.pow(atmpt.getAndIncrement(),2);
        if(delay > 1024) {
            delay = 1024;
        }
        System.out.println("Polynomial delay "+delay);
        Thread.sleep(delay);
    }
}
