package com.cko.thread;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程交互数据，数据交互只是在两个线程间，两个线程同时执行到 exchanger.exchange(data); 时就会执行交互，然后继续执行，等待是阻塞的
 */
public class T_10_Exchange {
    public static void main(String[] args) {

        ExecutorService service = Executors.newCachedThreadPool();
        Exchanger<String> exchanger = new Exchanger<String>();
        service.execute(new Runnable() {
            @Override
            public void run() {
                String data = "张三";
                System.out.println("ThreadName:" + Thread.currentThread().getName() + " 准备交换数据 " + data);

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    data = exchanger.exchange(data);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("ThreadName:" + Thread.currentThread().getName() + " 交换后数据 " + data);
            }
        });

        service.execute(new Runnable() {
            @Override
            public void run() {
                String data = "李四";
                System.out.println("ThreadName:" + Thread.currentThread().getName() + " 准备交换数据 " + data);

                try {
                    Thread.sleep(7000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    data = exchanger.exchange(data);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("ThreadName:" + Thread.currentThread().getName() + " 交换后数据 " + data);
            }
        });

        service.execute(new Runnable() {
            @Override
            public void run() {
                String data = "王五";
                System.out.println("ThreadName:" + Thread.currentThread().getName() + " 准备交换数据 " + data);

                try {
                    Thread.sleep(8000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    data = exchanger.exchange(data);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("ThreadName:" + Thread.currentThread().getName() + " 交换后数据 " + data);
            }
        });

        service.execute(new Runnable() {
            @Override
            public void run() {
                String data = "邹丽";
                System.out.println("ThreadName:" + Thread.currentThread().getName() + " 准备交换数据 " + data);

                try {
                    Thread.sleep(7000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    data = exchanger.exchange(data);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("ThreadName:" + Thread.currentThread().getName() + " 交换后数据 " + data);
            }
        });
    }
}
