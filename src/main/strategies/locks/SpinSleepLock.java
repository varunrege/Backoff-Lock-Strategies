package edu.vt.ece.hw4.locks;

import edu.vt.ece.hw4.bench.ThreadId;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;

public class SpinSleepLock implements Lock {
    static int LvThreads;
    int SpThreads;
    AtomicBoolean state = new AtomicBoolean(false);
    static boolean[] flag;
    static TTASLock lock = new TTASLock();
    static Object obj = new Object();

    public SpinSleepLock(int maxSpin, int threadCount) {
            LvThreads = maxSpin;
            SpThreads = 0;
            flag  = new boolean[threadCount];
            for(int i = 0; i< threadCount;i++)
            {
                flag[i] = false;
            }
        }

        public void resetSpinSleepLock() {
            SpThreads = 0;
        }

        @Override
        public void lock() {
            while (true) {
                while (state.get())
                {
                    int threadID = ((ThreadId) Thread.currentThread()).getThreadId();
                    if(!flag[threadID])
                    {
                        lock.lock();
                        if(SpThreads < LvThreads)
                        {
                            SpThreads++;
                            flag[threadID] = true;
                            lock.unlock();
                        }
                        else
                        {
                            lock.unlock();
                            synchronized(obj)
                            {
                                try {
                                    obj.wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        System.out.println("Thread "+threadID+" running, "+SpThreads+" threads spinning");
                    }
                }
                if (!state.getAndSet(true))
                {
                    return;
                }
            }
        }

        @Override
        public void unlock()
        {
            int threadID = ((ThreadId) Thread.currentThread()).getThreadId();
            if(flag[threadID])
            {
                lock.lock();
                SpThreads--;
                flag[threadID] = false;
                lock.unlock();
            }
            state.set(false);
            synchronized(obj)
            {
                obj.notifyAll();
            }
        }
}
