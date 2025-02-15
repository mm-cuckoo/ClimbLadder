package com.cko.thread;

import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock 重入锁，是实现Lock 接口的一个类，也是在实际编程中使用频率很高的一个锁，支持重入性，表示能够对共享资源重复加锁，
 * 即当前线程获取该锁后再次获取不会被阻塞。
 *
 * 公平和非公平
 * 默认是非公平锁
 * 可以通过构造函数设置公平锁 ReentrantLock(boolean fair) -》 ReentrantLock(true)
 */
public class T_11_00_ReentrantLock {

    private final ReentrantLock lock = new ReentrantLock();
    private int number = 0;
    public void addNumber() {
        lock.lock();
        try {
            number ++;
        }finally {
            lock.unlock();
        }
    }

    public int getNumber() {
        return number;
    }
    public static void main(String[] args) {
        T_11_00_ReentrantLock tReentrantLock = new T_11_00_ReentrantLock();

        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread() {
                @Override
                public void run() {
                    for (int j = 0; j < 1000; j++) {
                        tReentrantLock.addNumber();
                    }
                }
            };
            threads[i].start();
        }


        try {
            for (int i = 0; i < 10; i++) {
                threads[i].join();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("number:" + tReentrantLock.getNumber());

    }
}
