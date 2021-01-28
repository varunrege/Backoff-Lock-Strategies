package edu.vt.ece.hw4.locks;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

public class PriorityQueueLock implements Lock {

    static class PQNode {
        volatile boolean status = true;
        int pty = 0;
    }

    class Comp implements Comparator<PQNode> {
        public int compare (PQNode a, PQNode b) {
            if(a.pty < b.pty) {
                return 1;
            }
            else {
                return 0;
            }
        }
    }

    PriorityBlockingQueue<PQNode> nodequeue = new PriorityBlockingQueue<PQNode>(1, new Comp());
        ThreadLocal <PQNode> node;

    public PriorityQueueLock() {
        node = ThreadLocal.withInitial(() -> new PQNode());

    }

    @Override
    public void lock() {
        PQNode pqn = node.get();
        pqn.pty = Thread.currentThread().getPriority();
        nodequeue.add(pqn);
        if(nodequeue.peek() !=  null) {
            pqn.status = false;

            while(pqn.status) {
            }
        }
    }

    @Override
    public void unlock() {
    PQNode pqn = node.get();
    nodequeue.remove(pqn);
    PQNode node2 = nodequeue.peek();
    if (node2 != null) {
        node2.status = false;
        }
    //nodequeue.remove(pqn);
    }

    /*@Override
    public boolean trylock() {
        PQNode pqn = node.get();
        pqn.pty = Thread.currentThread().getPriority();
        nodequeue.add(pqn);
        if(nodequeue.peek() !=  pqn) {
            pqn.status = true;
            long t1 = System.currentTimeMillis();
            while(pqn.status) {
                long t2 = System.currentTimeMillis();

            }
        }
    }*/
}
