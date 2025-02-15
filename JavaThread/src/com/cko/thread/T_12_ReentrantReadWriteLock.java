package com.cko.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReentrantReadWriteLock 是 Java 的一种读写锁，它允许多个读线程同时访问，但只允许一个写线程访问（会阻塞所有的读写线程）。
 * 这种锁的设计可以提高性能，特别是在读操作的数量远远超过写操作的情况下。
 *
 * 在并发场景中，为了解决线程安全问题，我们通常会使用关键字 synchronized 或者 JUC 包中实现了 Lock 接口的 ReentrantLock。但它们都是独占式获取锁，
 * 也就是在同一时刻只有一个线程能够获取锁。
 *
 * 而在一些业务场景中，大部分只是读数据，写数据很少，如果仅仅是读数据的话并不会影响数据正确性，而如果在这种业务场景下，依然使用独占锁的话，
 * 很显然会出现性能瓶颈。针对这种读多写少的情况，Java 提供了另外一个实现 Lock 接口的 ReentrantReadWriteLock——读写锁。
 *
 * 锁降级：
 * 读写锁支持锁降级，遵循按照获取写锁，获取读锁再释放写锁的次序，写锁能够降级成为读锁，不支持锁升级，关于锁降级，
 *
 * 下面demo 中 只有公平锁会出现穿插交替执行情况，非公平锁时会出现先执行写，在执行读
 */
public class T_12_ReentrantReadWriteLock {

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    private boolean numberValid = false;

    private int number = 0;
    public void write() {
        writeLock.lock();
        System.out.println("get w lock -->");
        try {
            Thread.sleep(500);
            number++;
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("release w lock -->");
            writeLock.unlock();
        }
    }

    public int read() {
//        System.out.println("try get r lock -->");
        readLock.lock();
        System.out.println("------------.get r lock -->");
        try {
            return number;
        }finally {
            System.out.println("---------->release r lock -->");
            readLock.unlock();
        }
    }

    /**
     * 读写锁支持锁降级，遵循按照获取写锁，获取读锁再释放写锁的次序，写锁能够降级成为读锁，不支持锁升级，关于锁降级，
     *
     */
    public int writeAndRead(int i) {
        readLock.lock();
        if (!numberValid) {
            readLock.unlock();
            writeLock.lock();
            try {
                if (!numberValid) {
                    number = i;
                    numberValid = true;
                }

                readLock.lock();
            } finally {
                writeLock.unlock();
            }
        }


        try {
            return number;
        }finally {
            readLock.unlock();
        }

    }
    public static void main(String[] args) {
        T_12_ReentrantReadWriteLock rwLock = new T_12_ReentrantReadWriteLock();

        Thread[] threads = new Thread[20];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread() {
                @Override
                public void run() {
                    for (int j = 0; j < 2; j++) {
                        rwLock.write();
                    }
                }
            };
            threads[i].start();
        }

        for (int i = 10; i < 20; i++) {
            threads[i] = new Thread() {
                @Override
                public void run() {
                    for (int j = 0; j < 1000; j++) {
                        int number = rwLock.read();
                        System.out.println("------------->number:" + number);
                    }
                }
            };
            threads[i].start();
        }


        try {
            for (int i = 0; i < 20; i++) {
                threads[i].join();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}
