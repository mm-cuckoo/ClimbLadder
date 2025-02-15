package com.cko.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CountDownLatch 是一个同步工具类，它允许一个或多个线程一直等待，直到其他线程的操作执行完后再执行。
 *
 * CountDownLatch 有一个计数器，可以通过countDown()方法对计数器的数目进行减一操作，也可以通过await()方法来阻塞当前线程，直到计数器的值为 0。
 *
 * CountDownLatch 一般用来控制线程等待，它可以让某个线程一直等待直到倒计时结束，再开始执行。
 */
public class T_08_CountdownLatch {
    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();
        CountDownLatch cdOne = new CountDownLatch(1);
        CountDownLatch cdTwo = new CountDownLatch(3);
        for(int i = 0 ; i < 3 ; i ++){
            service.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("ThreadName:" + Thread.currentThread().getName() + " 第一小队 出发了");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("ThreadName:" + Thread.currentThread().getName() + "第一小队  到达等待处");
                    try {
                        cdOne.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("ThreadName:" + Thread.currentThread().getName() + "第一小队  继续前进  减掉一个门栓");
                    cdTwo.countDown();
                }
            });
        }

        service.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("ThreadName:" + Thread.currentThread().getName() + "第二小队  出发了，准备开门");
                try {
                    Thread.sleep(7000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                cdOne.countDown();
                System.out.println("ThreadName:" + Thread.currentThread().getName() + "第二小队  到达等待处");
                try {
                    cdTwo.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("ThreadName:" + Thread.currentThread().getName() + "第二小队  继续前进");
            }
        });
    }
}
