package edu.vt.ece.hw4.locks;

import edu.vt.ece.hw4.utils.ThreadCluster;
import edu.vt.ece.spin.HBOLock;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleHLock implements Lock {
    HBOLock[] locLock;
    HBOLock gLock = new HBOLock();
    AtomicBoolean gLockIndex;
    AtomicInteger[] locLockIndex;
    AtomicInteger gLockIndex2;
    AtomicInteger[] batch;
    //int BATCH_COUNT = 2;

    public SimpleHLock(int clusters) {
        locLock = new HBOLock[clusters];
        locLockIndex = new AtomicInteger[clusters];
        gLockIndex = new AtomicBoolean(false);
        gLockIndex2 = new AtomicInteger(-1);
        batch = new AtomicInteger[clusters];
        for (int a = 0; a < clusters; a++)
        {
            locLock[a] = new HBOLock();
            locLockIndex[a] = new AtomicInteger(0);
            batch[a] = new AtomicInteger(0);
        }
    }

    @Override
    public void lock() {
        int count = ThreadCluster.getCluster();
        locLockIndex[count].getAndIncrement();
        locLock[count].lock();
        //System.out.println("Thread "+ThreadCluster.get()+" in cluster "+count+" grabbed local lock");
        //locLockIndex[count].getAndDecrement();
        if (gLockIndex2.get() != count)
        {
            gLock.lock();
            //System.out.println("Thread "+ThreadCluster.get()+" in cluster "+count+" grabbed global lock");
            gLockIndex.set(true);
            gLockIndex2.set(count);
        }
    }

    @Override
    public void unlock() {
        int count = ThreadCluster.getCluster();

        locLock[count].unlock();
        locLockIndex[count].getAndDecrement();
        //System.out.println("Thread "+ThreadCluster.get()+" in cluster "+count+" released local lock");
        if (locLockIndex[count].get() == 0) //|| (batch[count].get() >= BATCH_COUNT))
        {
            gLock.unlock();
            //System.out.println("Thread "+ThreadCluster.get()+" in cluster "+count+" released global lock");
            gLockIndex.set(true);
            gLockIndex2.set(-1);
            batch[count].set(0);
        }
    }
}
