package com.cko.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Semaphore可以维护当前访问自身的线程个数，并提供了同步机制。使用Semaphore可以控制同时访问资源的线程个数，例如，实现一个文件允许的并发访问数。
 * Semaphore实现的功能就类似厕所有5个坑，假如有十个人要上厕所，那么同时能有多少个人去上厕所呢？同时只能有5个人能够占用，当5个人中的任何一个人让开后，其中在等待的另外5个人中又有一个可以占用了。
 * 另外等待的5个人中可以是随机获得优先机会，也可以是按照先来后到的顺序获得机会，这取决于构造Semaphore对象时传入的参数选项。
 * 单个信号量的Semaphore对象可以实现互斥锁的功能，并且可以是由一个线程获得了“锁”，再由另一个线程释放“锁”，这可应用于死锁恢复的一些场合。
 *
 * Semaphore 有两个主要的方法：acquire()和release()。acquire()方法会尝试获取一个信号量，如果获取不到，就会阻塞当前线程，直到有线程释放信号量。release()方法会释放一个信号量，释放之后，会唤醒一个等待的线程。
 * Semaphore 还有一个tryAcquire()方法，它会尝试获取一个信号量，如果获取不到，就会返回 false，不会阻塞当前线程。
 * Semaphore 用来控制同时访问某个特定资源的操作数量，它并不保证线程安全，所以要保证线程安全，还需要加上同步锁。
 */
public class T_07_Semaphore {
    public static void main(String[] args) {
        Semaphore sp = new Semaphore(3);
        ExecutorService service = Executors.newCachedThreadPool();

        for(int i = 0 ; i < 10; i ++){
            service.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        sp.acquire();//获取信号
                        System.out.println("线程" + Thread.currentThread().getName() + "进入，当前已有" + (3-sp.availablePermits()) + "个并发");
                        Thread.sleep((long)(Math.random()*10000));
                        System.out.println("线程" + Thread.currentThread().getName() + "即将离开");
                        sp.release();//释放信号
                        //下面代码有时候执行不准确，因为其没有和上面的代码合成原子单元
                        System.out.println("线程" + Thread.currentThread().getName() + "已离开，当前已有" + (3-sp.availablePermits()) + "个并发");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
