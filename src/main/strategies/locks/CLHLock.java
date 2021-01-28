package edu.vt.ece.hw4.locks;

/*
 * CLHLock.java
 *
 * Created on January 20, 2006, 11:35 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */


//import java.util.concurrent.locks.Lock;
//import java.util.concurrent.locks.Condition;
import java.lang.ThreadLocal;
//import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Craig-Hagersten-Landin Lock
 * @author Maurice Herlihy
 */
public class CLHLock implements Lock {
    // most recent lock holder
    AtomicReference<edu.vt.ece.spin.CLHLock.QNode> tail;
    // thread-local variables
    ThreadLocal<edu.vt.ece.spin.CLHLock.QNode> myNode, myPred;

    /**
     * Constructor
     */
    public CLHLock() {
        tail = new AtomicReference<edu.vt.ece.spin.CLHLock.QNode>(new edu.vt.ece.spin.CLHLock.QNode());
        // initialize thread-local variables
        myNode = new ThreadLocal<edu.vt.ece.spin.CLHLock.QNode>() {
            protected edu.vt.ece.spin.CLHLock.QNode initialValue() {
                return new edu.vt.ece.spin.CLHLock.QNode();
            }
        };
        myPred = new ThreadLocal<edu.vt.ece.spin.CLHLock.QNode>() {
            protected edu.vt.ece.spin.CLHLock.QNode initialValue() {
                return null;
            }
        };
    }

    public void lock() {
        edu.vt.ece.spin.CLHLock.QNode qnode = myNode.get(); // use my node
        qnode.locked = true;        // announce start
        // Make me the new tail, and find my predecessor
        edu.vt.ece.spin.CLHLock.QNode pred = tail.getAndSet(qnode);
        myPred.set(pred);           // remember predecessor
        while (pred.locked) {}      // spin
    }
    public void unlock() {
        edu.vt.ece.spin.CLHLock.QNode qnode = myNode.get(); // use my node
        qnode.locked = false;       // announce finish
        myNode.set(myPred.get());   // reuse predecessor
    }

    static class QNode {  // Queue node inner class
        volatile public boolean locked = false;
    }
}



