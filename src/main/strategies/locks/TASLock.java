package edu.vt.ece.hw4.locks;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
//import java.util.concurrent.locks.Lock;

public class TASLock implements Lock {
    AtomicBoolean state = new AtomicBoolean(false);
    public void lock() {
        while (state.getAndSet(true)) {} // ece.vt.edu.spin
    }
    public void unlock() {
        state.set(false);
    }
    // Any class that implements Lock must provide these methods.
    public Condition newCondition() {
        throw new UnsupportedOperationException();
    }
    public boolean tryLock(long time,
                           TimeUnit unit)
            throws InterruptedException {
        throw new UnsupportedOperationException();
    }
    public boolean tryLock() {
        throw new UnsupportedOperationException();
    }
    public void lockInterruptibly() throws InterruptedException {
        throw new UnsupportedOperationException();
    }
}