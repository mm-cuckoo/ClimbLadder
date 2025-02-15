package com.cko.thread;

public class T_02_ThreadLocal {
    /**
     *  下面这这段代码体现ThreadLocal在线程中使用的特性， 同一个ThreadLocal在不同线程中可以针对线程进行数据存储
     *  ThreadLocal 线程内数据共享，实现线程间数据隔离
     */
    private static final ThreadLocal<ThreadBean> sTl = new ThreadLocal<ThreadBean>(){
        @Override
        protected ThreadBean initialValue() {
            // 设置初始化数据
            return super.initialValue();
        }
    };
    public static void main(String[] args) {
        new Thread1().start();
        new Thread2().start();
    }

    static class Thread1 extends Thread {
        @Override
        public void run() {
            putMsg();
            getMsg();
        }

        public void putMsg() {
            sTl.set(new ThreadBean("thread1  local msg"));
        }

        public void getMsg() {
            ThreadBean value = sTl.get();
            System.out.println(value);
        }
    }

    static class Thread2 extends Thread {
        @Override
        public void run() {
            putMsg();
            getMsg();
        }

        public void putMsg() {
            sTl.set(new ThreadBean("thread2  local msg"));
        }

        public void getMsg() {
            ThreadBean value = sTl.get();
            System.out.println(value);
        }
    }

    static class ThreadBean {
        public String name;
        public ThreadBean(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "测试 Thread Local ~~ " + name;
        }
    }
}
