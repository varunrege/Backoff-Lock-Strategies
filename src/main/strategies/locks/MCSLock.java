/*
 * MCSLock.java
 *
 * Created on January 20, 2006, 11:41 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package edu.vt.ece.hw4.locks;

import java.util.concurrent.atomic.AtomicReference;

public class MCSLock implements Lock {
    AtomicReference<QNode> queue;
    ThreadLocal<QNode> myNode;

    public MCSLock() {
        queue = new AtomicReference<>(null);
        // initialize thread-local variable
        myNode = ThreadLocal.withInitial(() -> new QNode());
    }

    @Override
    public void lock() {
        QNode qnode = myNode.get();
        QNode pred = queue.getAndSet(qnode);
        if (pred != null) {
            qnode.locked = true;
            pred.next = qnode;
            while (qnode.locked) {
            }     // spin
        }
    }

    @Override
    public void unlock() {
        QNode qnode = myNode.get();
        if (qnode.next == null) {
            if (queue.compareAndSet(qnode, null))
                return;
            while (qnode.next == null) {
            } // spin
        }
        qnode.next.locked = false;
        qnode.next = null;
    }

    static class QNode {     // Queue node inner class
        volatile boolean locked = false;
        volatile QNode next = null;
    }
}

