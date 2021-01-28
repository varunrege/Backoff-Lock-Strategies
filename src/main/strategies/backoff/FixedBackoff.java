package edu.vt.ece.hw4.backoff;

import java.util.concurrent.atomic.AtomicInteger;

public class FixedBackoff implements Backoff {
    static AtomicInteger fail = new AtomicInteger(1);
    int[] array = {3,6,9,12,15,18,21,24,27,30};
    int atmpt;
    long delay;
    @Override
    public void backoff() throws InterruptedException {
        if((atmpt = fail.getAndIncrement()) < array.length) {
            delay = array[atmpt - 1];
        }
        else {
            delay = array[array.length - 1];
        }
        if(delay > 1024) {
            delay = 1024;
        }
        System.out.println("Fixed delay "+delay);
        Thread.sleep(delay);
    }
}
