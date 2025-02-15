package com.cko.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * demo:
 * 子线程1循环10次，子线程2循环20次，主线程循环100 次，子线程再循环10次，主线程循再环100 次，如此循环50 次,
 * 使用 Condition 实现多线程间互斥
 *
 * Condition 接口一共提供了以下 7 个方法：
 * await()：线程等待直到被通知或者中断。类似于 Object.wait()。
 * awaitUninterruptibly()：线程等待直到被通知，即使在等待时被中断也不会返回。没有与之对应的 Object 方法。
 * await(long time, TimeUnit unit)：线程等待指定的时间，或被通知，或被中断。类似于 Object.wait(long timeout)，但提供了更灵活的时间单位。
 * awaitNanos(long nanosTimeout)：线程等待指定的纳秒时间，或被通知，或被中断。没有与之对应的 Object 方法。
 * awaitUntil(Date deadline)：线程等待直到指定的截止日期，或被通知，或被中断。没有与之对应的 Object 方法。
 * signal()：唤醒一个等待的线程。类似于 Object.notify()。
 * signalAll()：唤醒所有等待的线程。类似于 Object.notifyAll()。
 *
 * 执行流程：
 * ReentrantLock 使用AQS 中有一个Node 同步队列
 * Condition 中维护一个单向的Node 等待队列
 * 当await后会把当前线程节点Node放入Condition中单向等待队列中
 * 当signal后会把Condition中的头节点Node取出放到ReentrantLock中的同步队列中执行
 *
 * 如果是多个Condition 就会有多个等待队列
 */
public class T_13_Condition {
    public static void main(String[] args) {
        RunFactory runFactory = new RunFactory();
        new Thread(new Runnable() {
            @Override
            public void run() {

                for(int i = 0 ; i < 50; i ++){
                    runFactory.sub1(i);
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {

                for(int i = 0 ; i < 50; i ++){
                    runFactory.sub2(i);
                }
            }
        }).start();
        for(int i = 0 ; i < 50; i ++){
            runFactory.main(i);
        }
    }

    static class RunFactory{
        private int isRunSub = 1;//是否是子线程在执行
        /**
         * 多线程间通信使用 Condition ，使用Condition 可以通知指定线程等待启动
         */
        Lock lock = new ReentrantLock();
        Condition conditionMain = lock.newCondition();
        Condition conditionSub1 = lock.newCondition();
        Condition conditionSub2 = lock.newCondition();

        public void sub1(int n){
            lock.lock();
            try {
                /**
                 *  使用while 防止假唤醒
                 */
                while(isRunSub != 1){
                    try {
                        //等待
                        conditionSub1.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                for(int i = 0 ; i < 10; i ++){
                    System.out.println("sub1 is run out-->" + n + "; in ---->" + i);
                }
                isRunSub = 2;
                conditionSub2.signal();//启动 conditionSub2 的等待
            } finally {
                lock.unlock();
            }
        }

        public void sub2(int n){
            lock.lock();
            try {
                /**
                 *  使用while 防止假唤醒
                 */
                while(isRunSub != 2){
                    try {
                        conditionSub2.await();//线程等待
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                for(int i = 0 ; i < 20; i ++){
                    System.out.println("sub2 is run out-->" + n + "; in ---->" + i);
                }
                isRunSub = 3;
                conditionMain.signal();
            } finally {
                lock.unlock();
            }
        }

        public void main(int n){
            lock.lock();
            try {
                /**
                 *  使用while 防止假唤醒
                 */
                while(isRunSub != 3){
                    try {
                        conditionMain.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                for(int i = 0 ; i < 100; i ++){
                    System.out.println("main is run out-->" + n + "; in ---->" + i);
                }
                isRunSub = 1;
                conditionSub1.signal();//唤醒一个线程等待
            } finally {
                lock.unlock();
            }
        }
    }
}

