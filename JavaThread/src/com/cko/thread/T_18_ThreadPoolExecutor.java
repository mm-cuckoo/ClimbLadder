package com.cko.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * ThreadPoolExecutor 构造参数说明：
 *    public ThreadPoolExecutor(int corePoolSize,
 *                               int maximumPoolSize,
 *                               long keepAliveTime,
 *                               TimeUnit unit,
 *                               BlockingQueue<Runnable> workQueue) {
 *         this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
 *              Executors.defaultThreadFactory(), defaultHandler);
 *     }
 *
 *  ThreadPoolExecutor 继承 AbstractExecutorService 和 ExecutorService
 *  在 AbstractExecutorService 中实现 ExecutorService submit 等方法
 *
 * corePoolSize：线程池中用来工作的核心线程数量。
 * maximumPoolSize：最大线程数，线程池允许创建的最大线程数。
 * keepAliveTime：超出 corePoolSize 后创建的线程存活时间或者是所有线程最大存活时间，取决于配置。
 * unit：keepAliveTime 的时间单位。
 * workQueue：任务队列，是一个阻塞队列，当线程数达到核心线程数后，会将任务存储在阻塞队列中。
 * threadFactory ：线程池内部创建线程所用的工厂。
 * handler：拒绝策略；当队列已满并且线程数量达到最大线程数量时，会调用该方法处理任务。
 *
 * 问题： 如果 maximumPoolSize > corePoolSize 为什么要等workQueue满了之后才会创建非核心线程去执行新的任务
 * 当有线程通过 execute 方法提交了一个任务，首先会去判断当前线程池的线程数是否小于核心线程数，也就是线程池构造时传入的参数 corePoolSize。
 * 如果小于，那么就直接通过 ThreadFactory 创建一个线程来执行这个任务，当任务执行完之后，线程不会退出，而是会去阻塞队列中获取任务，接下来如果又提交了一个任务，
 * 也会按照上述的步骤去判断是否小于核心线程数，如果小于，还是会创建线程来执行任务，执行完之后也会从阻塞队列中获取任务。这里有个细节，就是提交任务的时候，
 * 就算有线程池里的线程从阻塞队列中获取不到任务，如果线程池里的线程数还是小于核心线程数，那么依然会继续创建线程，而不是复用已有的线程。
 * 如果线程池里的线程数不再小于核心线程数呢？那么此时就会尝试将任务放入阻塞队列中，入队成功之后，这样，阻塞的线程就可以获取到任务了。
 * 但是，随着任务越来越多，队列已经满了，任务放入失败，此时会判断当前线程池里的线程数是否小于最大线程数，也就是入参时的 maximumPoolSize 参数
 * 如果小于最大线程数，那么也会创建非核心线程来执行提交的任务，所以，就算队列中有任务，新创建的线程还是会优先处理这个提交的任务，而不是从队列中获取已有的任务执行，
 * 从这可以看出，先提交的任务不一定先执行。
 *
 * JDK 自带的 RejectedExecutionHandler 实现有 4 种
 * AbortPolicy：丢弃任务，抛出运行时异常
 * CallerRunsPolicy：由提交任务的线程来执行任务
 * DiscardPolicy：丢弃这个任务，但是不抛异常
 * DiscardOldestPolicy：从队列中剔除最先进入队列的任务，然后再次提交任务
 * 线程池创建的时候，如果不指定拒绝策略就默认是 AbortPolicy 策略。
 *
 * 监控线程执行方法：
 * ThreadPoolExecutor 中有beforeExecute 回调函数，在
 */
public class T_18_ThreadPoolExecutor {

    private final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private final int CORE_THREAD_COUNT = CPU_COUNT + 1;
    private final int MAX_THREAD_COUNT = CPU_COUNT * 2;

    /**
     * 队列满了后并且没有多余的非核心线程执行后，任务会被放到这里
     */
    private final RejectedExecutionHandler reh = new RejectedExecutionHandler(){

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            String value = "RejectedExecution Runnable name:" + r.getClass().getSimpleName();
            System.out.println(value);
        }
    };

    /**
     * 创建线程的工厂
     */
    private final ThreadFactory tf = new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            System.out.println("create new thread : " + r.getClass().getSimpleName());
            return new Thread(r);
        }
    };

    private final ExecutorService executor = new ThreadPoolExecutor(
            CORE_THREAD_COUNT, MAX_THREAD_COUNT, 5L,
            TimeUnit.SECONDS, new ArrayBlockingQueue<>(5), tf, reh
    ) {

        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            // 执行之前回调
            if (r instanceof RunWhen) {
                ((RunWhen) r).before();
            }
        }
        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            //执行之后回调
            if (r instanceof RunWhen) {
                ((RunWhen) r).after();
            }
        }
    };

    interface RunWhen {
        public void before();
        public void after();
    }

    private static class ThreadCallable implements Callable<String>, RunWhen {

        private Long startTime;
        @Override
        public String call()  {
            long sleepTime = (long) (int)(Math.random() * 1000);
            System.out.println("run start " + sleepTime);
            String value = "call method run :" + sleepTime;
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println("run end " + value);
            return value;
        }

        @Override
        public void before() {
            startTime = System.currentTimeMillis();
        }

        @Override
        public void after() {
            long useTime = System.currentTimeMillis() - startTime;
            System.out.println(Thread.currentThread().getName() + " use time:" + useTime);
        }
    }

    private static class ThreadRunnable implements Runnable , RunWhen{
        private Long startTime;

        @Override
        public void run() {
            long sleepTime = (long) (int)(Math.random() * 1000);
            System.out.println("run start " + sleepTime);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println("run end ");
        }

        @Override
        public void before() {
            startTime = System.currentTimeMillis();
        }

        @Override
        public void after() {
            long useTime = System.currentTimeMillis() - startTime;
            System.out.println(Thread.currentThread().getName() + " use time:" + useTime);
        }
    }

    public void exe1() {
        for (int i = 0; i < 50; i++) {
            executor.execute(new ThreadRunnable());
        }
        executor.shutdown();
    }

    public void exe2() {
        List<FutureTask<String>> futureTasks = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            FutureTask<String> futureTask = new FutureTask<>(new ThreadCallable());
            futureTasks.add(futureTask);
            executor.execute(futureTask);
        }

        for (int i = 0; i < 50; i++) {
            try {
                System.out.println("call able back :" + futureTasks.get(i).get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
    }

    public void exe3() {

        List<Future<String>> futureList = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            futureList.add(executor.submit(new ThreadCallable()));
        }

        for (Future<String> stringFuture : futureList) {
            try {
                System.out.println("call able back :" + stringFuture.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
    }
    public static void main(String[] args) {
        T_18_ThreadPoolExecutor executor = new T_18_ThreadPoolExecutor();
//        executor.exe1();
//        executor.exe2();
        executor.exe3();


    }

}
