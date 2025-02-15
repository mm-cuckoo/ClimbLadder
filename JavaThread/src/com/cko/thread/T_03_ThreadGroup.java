package com.cko.thread;

public class T_03_ThreadGroup {

    public static void main(String[] args) {
        T_03_ThreadGroup group = new T_03_ThreadGroup();
        group.group1();
    }


    /**
     * 统一处理异常, 当线程抛出异常时，异常会被ThreadGroup 中 uncaughtException
     */
    public void group1() {
        ThreadGroup group = new ThreadGroup("ex-group"){
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                super.uncaughtException(t, e);
                System.out.println("t-name:" + t.getName()  +     "e:" + e.getMessage());
            }
        };

        new Thread(group, new Runnable() {
            @Override
            public void run() {
                throw new RuntimeException("thread-1 exp");
            }
        }).start();

        new Thread(group, new Runnable() {
            @Override
            public void run() {
                throw new RuntimeException("thread-2 exp");
            }
        }).start();


    }
}
