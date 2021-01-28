package edu.vt.ece.hw4;

import edu.vt.ece.hw4.barriers.Barrier;
import edu.vt.ece.hw4.barriers.FinalBarrier;
import edu.vt.ece.hw4.bench.*;
import edu.vt.ece.hw4.locks.*;
import edu.vt.ece.hw4.utils.ThreadCluster;
import edu.vt.ece.spin.HBOLock;

public class Benchmark {

    private static final String ALOCK = "ALock";
    private static final String BACKOFFLOCK = "BackoffLock";
    private static final String MCSLOCK = "MCSLock";
    private static final String CLHLOCK = "CLHLock";
    private static final String TASLOCK = "TASLock";
    private static final String TTASLOCK = "TTASLock";
    private static final String PBQLOCK = "PriorityQueueLock";
    private static final String SPINSLEEPLOCK = "SpinSleepLock";
    private static final String SIMPLEHLOCK = "SimpleHLock";
    private static final String HBOLOCK = "HBOLock";
    private static final String BARRIERLOCK = "BarrierLock";

    public static void main(String[] args) throws Exception {
        String mode = args.length <= 0 ? "long" : args[0];
        String lockClass = (args.length <= 1 ? SPINSLEEPLOCK : args[1]);
        int threadCount = (args.length <= 4 ? 8 : Integer.parseInt(args[2]));
        int totalIters = (args.length <= 3 ? 64000 : Integer.parseInt(args[3]));
        int iters = totalIters / threadCount;

        run(args, mode, lockClass, threadCount, iters);

    }

    private static void run(String[] args, String mode, String lockClass, int threadCount, int iters) throws Exception {
        for (int i = 0; i < 5; i++) {
            Lock lock = null;
            switch (lockClass.trim()) {
                case ALOCK:
                    lock = new ALock(threadCount);
                    break;
                case BACKOFFLOCK:
                    lock = new BackoffLock("Fibonacci");
                    break;
                case MCSLOCK:
                    lock = new MCSLock();
                    break;
                case CLHLOCK:
                    lock = new CLHLock();
                    break;
                case TASLOCK:
                    lock = new TASLock();
                    break;
                case TTASLOCK:
                    lock = new TTASLock();
                    break;
                /*case BARRIERLOCK:
                    lock = new TTASBarrier();
                    break;*/
                case PBQLOCK:
                    lock = new PriorityQueueLock();
                    break;
                case SPINSLEEPLOCK:
                    lock = new SpinSleepLock(2, threadCount);
                    break;
                case SIMPLEHLOCK:
                    lock = new SimpleHLock(threadCount/2);
                    break;
                case HBOLOCK:
                    lock = new HBOLock();
                    break;
            }

            switch (mode.trim().toLowerCase()) {
                case "normal":
                    Counter counter = new SharedCounter(0, lock);
                    runNormal(counter, threadCount, iters);
                    break;
                case "empty":
                    runEmptyCS(lock, threadCount, iters);
                    break;
                case "long":
                    runLongCS(lock, threadCount, iters);
                    break;
                case "priority":
                    Counter counter2 = new SharedCounter(0, lock);
                    runPBQTest(counter2, threadCount, iters);
                    break;
                case "cluster":
                    Counter counter1 = new SharedCounter(0, lock);
                    runClusterCS(counter1, threadCount, iters);
                    break;
                case "barrier":
                    Barrier b = new FinalBarrier();
                    break;
                    //throw new UnsupportedOperationException("Complete this.");

                //default:
                  //  throw new UnsupportedOperationException("Implement this");
               }
        }
    }

    private static void runNormal(Counter counter, int threadCount, int iters) throws Exception {
        final TestThread[] threads = new TestThread[threadCount];
        TestThread.reset();

        for (int t = 0; t < threadCount; t++) {
            threads[t] = new TestThread(counter, iters);
        }

        for (int t = 0; t < threadCount; t++) {
            threads[t].start();
        }

        long totalTime = 0;
        for (int t = 0; t < threadCount; t++) {
            threads[t].join();
            totalTime += threads[t].getElapsedTime();
        }

        System.out.println("Average time per thread is " + totalTime / threadCount + "ms");
    }

    private static void runEmptyCS(Lock lock, int threadCount, int iters) throws Exception {

        final EmptyCSTestThread[] threads = new EmptyCSTestThread[threadCount];
        EmptyCSTestThread.reset();

        for (int t = 0; t < threadCount; t++) {
            threads[t] = new EmptyCSTestThread(lock, iters);
        }

        for (int t = 0; t < threadCount; t++) {
            threads[t].start();
        }

        long totalTime = 0;
        for (int t = 0; t < threadCount; t++) {
            threads[t].join();
            totalTime += threads[t].getElapsedTime();
        }

        System.out.println("Average time per thread is " + totalTime / threadCount + "ms");
    }

    static void runLongCS(Lock lock, int threadCount, int iters) throws Exception {
        final Counter counter = new Counter(0);
        final LongCSTestThread[] threads = new LongCSTestThread[threadCount];
        LongCSTestThread.reset();

        for (int t = 0; t < threadCount; t++) {
            threads[t] = new LongCSTestThread(lock, counter, iters);
        }

        for (int t = 0; t < threadCount; t++) {
            threads[t].start();
        }

        long totalTime = 0;
        for (int t = 0; t < threadCount; t++) {
            threads[t].join();
            totalTime += threads[t].getElapsedTime();
        }

        System.out.println("Average time per thread is " + totalTime / threadCount + "ms");
    }
    private static void runBarrierLock(Counter counter, int threadCount, int iters) throws Exception {
        final TestThread[] threads = new TestThread[threadCount];
        TestThread.reset();

        for (int t = 0; t < threadCount; t++) {
            threads[t] = new TestThread(counter, iters);
        }

        for (int t = 0; t < threadCount; t++) {
            threads[t].start();
        }

        long totalTime = 0;
        for (int t = 0; t < threadCount; t++) {
            threads[t].join();
            totalTime += threads[t].getElapsedTime();
        }

        System.out.println("Average time per thread is " + totalTime / threadCount + "ms");
    }

    private static void runPBQTest(Counter counter2, int threadCount, int iters) throws Exception {
        final PriorityQueueLockTest[] threads = new PriorityQueueLockTest[threadCount];
        TestThread.reset();

        for (int t = 0; t < threadCount; t++) {
            threads[t] = new PriorityQueueLockTest(counter2, iters);
        }

        for (int t = 0; t < threadCount; t++) {
            threads[t].start();
        }

        long totalTime = 0;
        for (int t = 0; t < threadCount; t++) {
            threads[t].join();
            totalTime += threads[t].getElapsedTime();
        }

        System.out.println("Average time per thread is " + totalTime / threadCount + "ms");
    }
    private static void runClusterCS(Counter counter, int threadCount, int iters) throws Exception {
        final TestThread[] threads = new TestThread[threadCount];
        ThreadCluster.reset();

        for (int t = 0; t < threadCount; t++) {
            threads[t] = new TestThread(counter, iters);
        }

        for (int t = 0; t < threadCount; t++) {
            threads[t].start();
        }

        long totalTime = 0;
        for (int t = 0; t < threadCount; t++) {
            threads[t].join();
            totalTime += threads[t].getElapsedTime();
        }

        System.out.println("Average time per thread is " + totalTime / threadCount + "ms");
    }
}


