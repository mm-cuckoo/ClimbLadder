package com.cko.thread;

import java.util.concurrent.locks.LockSupport;

/**
 * LockSupprot 用来阻塞和唤醒线程，底层实现依赖于 Unsafe 类
 * 该类包含一组用于阻塞和唤醒线程的静态方法，这些方法主要是围绕 park 和 unpark
 *
 * API
 * void park()：阻塞当前线程，如果调用 unpark 方法或线程被中断，则该线程将变得可运行。请注意，park 不会抛出 InterruptedException，因此线程必须单独检查其中断状态。
 * void park(Object blocker)：功能同方法 1，入参增加一个 Object 对象，用来记录导致线程阻塞的对象，方便问题排查。
 * void parkNanos(long nanos)：阻塞当前线程一定的纳秒时间，或直到被 unpark 调用，或线程被中断。
 * void parkNanos(Object blocker, long nanos)：功能同方法 3，入参增加一个 Object 对象，用来记录导致线程阻塞的对象，方便问题排查。
 * void parkUntil(long deadline)：阻塞当前线程直到某个指定的截止时间（以毫秒为单位），或直到被 unpark 调用，或线程被中断。
 * void parkUntil(Object blocker, long deadline)：功能同方法 5，入参增加一个 Object 对象，用来记录导致线程阻塞的对象，方便问题排查。
 *
 * 设计思路
 * LockSupport 的设计思路是通过许可证来实现的，就像汽车上高速公路，入口处要获取通行卡，出口处要交出通行卡，如果没有通行卡你就无法出站，当然你可以选择补一张通行卡。
 *
 * LockSupport 会为使用它的线程关联一个许可证（permit）状态，permit 的语义「是否拥有许可」，0 代表否，1 代表是，默认是 0。
 *
 * LockSupport.unpark：指定线程关联的 permit 直接更新为 1，如果更新前的permit<1，唤醒指定线程
 * LockSupport.park：当前线程关联的 permit 如果>0，直接把 permit 更新为 0，否则阻塞当前线程
 *
 * 如果先LockSupport.unpark后面在调用  LockSupport.park 不会阻塞线程ßß
 *
 * 与 synchronzed 的区别
 * 有一点需要注意的是：synchronzed 会使线程阻塞，线程会进入 BLOCKED 状态，而调用 LockSupprt 方法阻塞线程会使线程进入到 WAITING 状态。
 *
 * thread 线程调用 LockSupport.park() 使 thread 阻塞，当 mian 线程睡眠 3 秒结束后通过 LockSupport.unpark(thread) 方法唤醒 thread 线程，thread 线程被唤醒后会执行后续的操作。
 * 另外，LockSupport.unpark(thread)可以指定线程对象唤醒指定的线程。
 */
public class T_14_LockSupport {

    public void lock1() {

        Thread thread = new Thread() {
            @Override
            public void run() {
                System.out.println("park before --->");
                LockSupport.park();
                System.out.println("park after --->");
            }
        };

        thread.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("unpark --->" + thread.getState());
        LockSupport.unpark(thread);
    }

    /**
     * 如果线程启动之后，在park前 unpark ,此时park 会直接执行
     */
    public void lock2() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                System.out.println("park before --->");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                LockSupport.park();
                System.out.println("park after --->");
            }
        };


        thread.start();
        System.out.println("unpark --->");
        LockSupport.unpark(thread);
    }

    public static void main(String[] args) {
        T_14_LockSupport lockSupport = new T_14_LockSupport();
        lockSupport.lock1();
//        lockSupport.lock2();

    }
}
