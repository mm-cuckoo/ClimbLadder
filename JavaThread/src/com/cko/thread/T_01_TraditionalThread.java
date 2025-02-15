package com.cko.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class T_01_TraditionalThread {

    public static void main(String[] args) {
        T_01_TraditionalThread t = new T_01_TraditionalThread();
//        t.createThread1();
//        t.createThread2();
//        t.createThread3();
        t.createThread4();
    }

    /**
     * 重写Thread 的Run 函数
     */
    public void  createThread1() {
        new Thread() {
            @Override
            public void run() {
                System.out.println("重写Thread 的Run 函数");
            }
        }.start();

    }

    /**
     * 创建时设置Thread target (Runnable) 参数
     */
    public void createThread2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("创建时设置Thread target (Runnable) 参数");
            }
        }).start();
    }

    /**
     * 重写Thread 的Run 函数同时设置Thread target (Runnable) 参数
     * 这里面需要注意调用顺序， 关键点在于 run() 函数是否调用 super.run(); 函数
     * 首先要明确，无论何种写法 run() 函数一定会被调用， 看一下源码中 run() 函数实现
     *     public void run() {
     *         if (target != null) {
     *             target.run();
     *         }
     *     }
     *  源码中 target 即为 创建Thread 时传入的 Runnable 对象,可以看到如果从写了Thread run() 函数，是否调用 super.run();
     *  函数将影响创建Thread 时传入的 Runnable 对象是否被执行
     */
    public void createThread3() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("实例Thread时的Runnable ~~~~");
            }
        }){
            @Override
            public void run() {
                System.out.println("实例Thread Run() 1 ~~~~");
                super.run();
                System.out.println("实例Thread Run() 2 ~~~~");
            }
        }.start();
    }

    /**
     * 带有返回值的线程
     */
    public void createThread4() {
        Callable<String> call = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "thread call method";
            }
        };

        /**
         * FutureTask 是Future的实现， 并且实现Runnable接口，通过Runnable接口run函数调用Callable接口的call 函数
         * task中get会阻塞线程执行，直到线程返回结果
         */
        FutureTask<String> task = new FutureTask<>(call);
        Thread thread = new Thread(task);
        thread.start();

        try {
            String result = task.get();
            System.out.println("result--->" + result);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

}
