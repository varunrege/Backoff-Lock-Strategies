package edu.vt.ece.hw4.barriers;


import edu.vt.ece.hw4.locks.Lock;

public class barrier1 {

    private Lock lock;
    static int count;

    public barrier1(int count, Lock lock) {
        this.lock = lock;
        this.count = count;
    }

    public void TTASbarrier() {
        lock.lock();
        try {
            count = count + 1;

        } finally {
            lock.unlock();
        }
    }
}