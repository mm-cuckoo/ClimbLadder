package com.cko.thread;

import java.util.concurrent.locks.StampedLock;

/**
 * StampedLock 类是 Java 8 才发布的，也是 Doug Lea 大神所写，有人称它为锁的性能之王。
 *
 * StampedLock 没有实现 Lock 接口和 ReadWriteLock 接口，但它实现了“读写锁”的功能，
 * 并且性能比 ReentrantReadWriteLock 更高。StampedLock 还把读锁分为了“乐观读锁”和“悲观读锁”两种。
 *
 * 它的核心思想在于，在读的时候如果发生了写，应该通过重试的方式来获取新的值，而不应该阻塞写操作。
 * 这种模式也就是典型的无锁编程思想，和 CAS 自旋的思想一样。这种操作方式决定了 StampedLock 在读线程非常多而写线程非常少的场景下非常适用，
 * 同时还避免了写饥饿情况的发生。
 *
 * ReentrantReadWriteLock 和 StampedLock 对比
 * 1、可重入性：ReentrantReadWriteLock 支持可重入，即在一个线程中可以多次获取读锁或写锁。StampedLock 则不支持可重入。
 * 2、乐观读锁：StampedLock 提供了乐观读锁机制，允许一个线程在没有任何写入操作发生的情况下读取数据，从而提高了性能。
 *    而 ReentrantReadWriteLock 没有提供这样的机制。
 * 3、锁降级：StampedLock 提供了从写锁到读锁的降级功能，这在某些场景下可以提供额外的灵活性。ReentrantReadWriteLock 不直接提供这样的功能。
 * 4、API 复杂性：由于提供了乐观读锁和锁降级功能，StampedLock 的 API 相对复杂一些，需要更小心地使用以避免死锁和其他问题。
 *    ReentrantReadWriteLock 的 API 相对更直观和容易使用。
 *
 * 综上所述，StampedLock 提供了更高的性能和灵活性，但也带来了更复杂的使用方式。ReentrantReadWriteLock 则相对简单和直观，特别适用于没有高并发读的场景。
 */
public class T_06_StampedLock {

    StampedLock stampedLock = new StampedLock();

    private int numnber = 0;

    public void write(int i ) {
        long stamp = stampedLock.writeLock(); // 获取写锁
        try {
            numnber = i;
        } finally {
            stampedLock.unlockWrite(stamp); // 释放写锁
        }
    }

    public int read() {
        long stamp = stampedLock.tryOptimisticRead(); // 获取乐观锁读锁
        int current = numnber;
        if (!stampedLock.validate(stamp)) { //检查乐观读锁后是否有其他写锁发生，有则返回false
            stamp = stampedLock.readLock(); // 获取悲观读锁
            try {
                current = numnber;
            } finally {
                stampedLock.unlockRead(stamp); // 释放悲观读锁
            }
        }

        return current;
    }

    /**
     * 在获取读锁的情况下转化为写锁
     * @param i
     */
    public void readUpWrite(int i) {
        long stamp = stampedLock.readLock();
        try {
            // 读锁尝试转换为写锁：转换成功后相当于获取了写锁，转换失败相当于有写锁被占用
            long ws = stampedLock.tryConvertToWriteLock(stamp);
            if (ws != 0) { // 不为0 转化成功
                stamp = ws;
                System.out.println("covert success ---->stamp :" + stamp  + "  ws:" + ws);
            } else {
                stampedLock.unlockRead(stamp); // 释放读锁
                stamp = stampedLock.writeLock(); // 获取独占写锁
                System.out.println("get write lock --->");
            }
            numnber = i;
        }finally {
            stampedLock.unlock(stamp);
        }

    }

    public static void main(String[] args) {
        T_06_StampedLock stampedLock = new T_06_StampedLock();

        Thread write = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    stampedLock.write(i);
                    System.out.println("write:" + i);
                }
            }
        };

        Thread read = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    int number = stampedLock.read();
                    System.out.println("read:" + number);
                }
            }
        };

        Thread rw = new Thread() {
            @Override
            public void run() {
                for (int i = 10; i < 20; i++) {
                    stampedLock.readUpWrite(i);
                    System.out.println("readUpWrite:" + i);
                }
            }
        };

        write.start();
        read.start();
        rw.start();
    }
}
