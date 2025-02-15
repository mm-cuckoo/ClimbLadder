package com.cko.thread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *表示大家彼此等待，大家集合好后才开始出发，分散活动后又在指定地点集合碰面，这就好比整个公司的人员利用周末时间集体郊游一样，先各自从家出发到公司集合后，
 * 再同时出发到公园游玩，在指定地点集合后再同时开始就餐，…。
 *
 * CyclicBarrier 是一个同步工具类，它允许一组线程互相等待，直到到达某个公共屏障点（common barrier point）。
 *
 * CyclicBarrier 可以用于多线程计算数据，最后合并计算结果的应用场景。比如我们用一个 Excel 保存了用户所有银行流水，每个 sheet 保存一个账户近一年的每笔银行流水，现在需要统计用户的日均银行流水，先用多线程处理每个 sheet 里的银行流水，都执行完之后，得到每个 sheet 的日均银行流水，最后，再用 barrierAction 用这些线程的计算结果，计算出整个 Excel 的日均银行流水。
 *
 * CyclicBarrier 的计数器可以通过reset()方法重置，所以它能处理循环使用的场景。比如，我们将一个大任务分成 10 个小任务，用 10 个线程分别执行这 10 个小任务，当 10 个小任务都执行完之后，再合并这 10 个小任务的结果，这个时候就可以用 CyclicBarrier 来实现。
 *
 * CyclicBarrier 还有一个有参构造方法，可以指定一个 Runnable，这个 Runnable 会在 CyclicBarrier 的计数器为 0 的时候执行，用来完成更复杂的任务。
 */
public class T_09_CyclicBarrier {


    public static void main(String[] args) {
        CyclicBarrier cb = new CyclicBarrier(3, new Runnable() {
            @Override
            public void run() {
                System.out.println("都集合了，出发");
            }
        });
        ExecutorService service = Executors.newCachedThreadPool();
        for(int i = 0 ; i < 12; i ++){
            service.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep((long)(Math.random()*10000));
                        System.out.println("线程" + Thread.currentThread().getName() +
                                "即将到达集合地点1，当前已有" + cb.getNumberWaiting() + "个已经到达，正在等候");
                        cb.await();//只有线程到达设定值时，才会向下执行
                        System.out.println("线程" + Thread.currentThread().getName() +
                                "即将到达集合地点3，当前已有" + cb.getNumberWaiting() + "个已经到达，出发");
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        service.shutdown();
    }
}
