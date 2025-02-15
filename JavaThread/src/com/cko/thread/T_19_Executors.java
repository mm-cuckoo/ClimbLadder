package com.cko.thread;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 *
 */
public class T_19_Executors {
    private ExecutorService exe;
    public void runFixedThreadPool(){
        /**
         *  创建固定大小线程池
         *     public static ExecutorService newFixedThreadPool(int nThreads) {
         *         return new ThreadPoolExecutor(nThreads, nThreads,
         *                                       0L, TimeUnit.MILLISECONDS,
         *                                       new LinkedBlockingQueue<Runnable>());
         *     }
         *
         *     newFixedThreadPool 设置的核心线程数和最大线程数一致，并且LinkedBlockingQueue使用默认值，
         *     所以可以一直向队列中一直添加执行事务直到内存耗尽
         *     下面创建2个线程来执行10 个事务，所以开始时，2 个线程进入执行状态后，后面的事务都将放入执行队列中
         */
        exe = Executors.newFixedThreadPool(2);
//        submitRunnable(exe);
        submitCallable(exe);

    }

    public void runCachedThreadPool() {

        /**
         *  可缓存的线程池
         *     public static ExecutorService newCachedThreadPool() {
         *         return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
         *                                       60L, TimeUnit.SECONDS,
         *                                       new SynchronousQueue<Runnable>());
         *     }
         *
         *  它的核心线程数为0，最大线程数为Integer.MAX_VALUE，即可以创建任意多的线程。空闲线程等待新任务的最长时间为60秒，超过这个时间后，多余空闲线程将被终止。
         *  这种线程池适合执行大量短期的异步任务。
         */
        exe = Executors.newCachedThreadPool();
        submitRunnable(exe);
    }

    public void runSingleThreadExecutor() {
        /**
         * 单线程化的线程池
         *     public static ExecutorService newSingleThreadExecutor() {
         *         return new FinalizableDelegatedExecutorService
         *             (new ThreadPoolExecutor(1, 1,
         *                                     0L, TimeUnit.MILLISECONDS,
         *                                     new LinkedBlockingQueue<Runnable>()));
         *     }
         *
         *  它只用唯一的工作线程来执行任务，保证所有任务按照指定顺序（FIFO, LIFO, 优先级）执行。
         *  创建只有一个线程的线程池，在该线程池中如果线程意外停止，该线程池会启动一个新的线程继续执行任务
         */
        exe = Executors.newSingleThreadExecutor();
        submitRunnable(exe);
    }

    public void run() {
        ScheduledExecutorService exe = Executors.newScheduledThreadPool(1);

        /**
         * 安排一个Runnable任务在给定的延迟后执行。
         *
         * @param command 需要执行的任务
         * @param delay 延迟时间
         * @param unit 时间单位
         * @return 可用于提取结果或取消的ScheduledFuture
         */
        exe.schedule(new Runnable() {
            @Override
            public void run() {

            }
        }, 1 , TimeUnit.SECONDS);

        /**
         * 安排一个Runnable任务在给定的初始延迟后首次执行，随后每个period时间间隔执行一次。
         * scheduleAtFixedRate 方法在initialDelay时长后第一次执行任务，以后每隔period时长再次执行任务。
         * 注意，period 是从任务开始执行算起的。开始执行任务后，定时器每隔 period 时长检查该任务是否完成，如果完成则再次启动任务，否则等该任务结束后才启动任务。
         *
         * @param command 需要执行的任务
         * @param initialDelay 首次执行的初始延迟
         * @param period 连续执行之间的时间间隔
         * @param unit 时间单位
         * @return 可用于提取结果或取消的ScheduledFuture
         */
        exe.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

            }
        }, 1,  2, TimeUnit.SECONDS);

        /**
         * 安排一个Runnable任务在给定的初始延迟后首次执行，随后每次完成任务后等待指定的延迟再次执行。
         *
         * @param command 需要执行的任务
         * @param initialDelay 首次执行的初始延迟
         * @param delay 每次执行结束后的延迟时间
         * @param unit 时间单位
         * @return 可用于提取结果或取消的ScheduledFuture
         */
        exe.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {

            }
        }, 1, 2,TimeUnit.SECONDS);
    }

    public void submitRunnable(ExecutorService exe) {
        for (int i = 0; i <10; i++) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    System.out.println("start  t-name:" + Thread.currentThread().getName());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("end  t-name:" + Thread.currentThread().getName());
                }
            };

            exe.submit(runnable);
        }

        exe.shutdown();
    }

    /**
     * 可以通过 submit callable 方式添加阻塞等待返回值的线程调用
     */
    public void  submitCallable(ExecutorService exe) {
        List<Future<String>> futureList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Callable<String> callable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    String value = "t-name:" + Thread.currentThread().getName() + " - " + (int)( Math.random() * 100);
                    System.out.println("return:" + value);
                    return value;
                }
            };
            futureList.add(exe.submit(callable));
        }

        for (Future<String> feature: futureList) {
            try {
                System.out.println("get:" + feature.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }

        }
        exe.shutdown();
    }

    public static void main(String[] args) {
        T_19_Executors use = new T_19_Executors();
//        use.runFixedThreadPool();
//        use.runCachedThreadPool();
        use.runSingleThreadExecutor();
    }
}
