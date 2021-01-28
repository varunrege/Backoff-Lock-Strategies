package edu.vt.ece.hw4.backoff;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Balaji Arun
 */
public class ExponentialBackoff implements Backoff {
    static AtomicInteger atmpt = new AtomicInteger(1);
    @Override
    public void backoff() throws InterruptedException {
        long delay = (long)Math.pow(2,atmpt.getAndIncrement());
        if(delay > 1024) {
            delay = 1024;
        }
        System.out.println("Exponential delay "+delay);
        Thread.sleep(delay);
    }
}




