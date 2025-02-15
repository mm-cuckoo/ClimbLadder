package com.cko.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *由于 BlockingQueue 继承了 Queue 接口，因此，BlockingQueue 也具有 Queue 接口的基本操作，如下所示：
 * #1）插入元素
 * boolean add(E e) ：将元素添加到队列尾部，如果队列满了，则抛出异常 IllegalStateException。
 * boolean offer(E e)：将元素添加到队列尾部，如果队列满了，则返回 false。
 *
 * #2）删除元素
 * boolean remove(Object o)：从队列中删除元素，成功返回true，失败返回false
 * E poll()：检索并删除此队列的头部，如果此队列为空，则返回null。
 *
 * #3）查找元素
 * E element()：检索但不删除此队列的头部，如果队列为空时则抛出 NoSuchElementException 异常；
 * peek()：检索但不删除此队列的头部，如果此队列为空，则返回 null.
 *
 * 除了从 Queue 接口 继承到一些方法，BlockingQueue 自身还定义了一些其他的方法，比如说插入操作：
 *
 * void put(E e)：将元素添加到队列尾部，如果队列满了，则线程将阻塞直到有空间。
 * offer(E e, long timeout, TimeUnit unit)：将指定的元素插入此队列中，如果队列满了，则等待指定的时间，直到队列可用。
 *
 * 比如说删除操作：
 * take()：检索并删除此队列的头部，如有必要，则等待直到队列可用；
 * poll(long timeout, TimeUnit unit)：检索并删除此队列的头部，如果需要元素变得可用，则等待指定的等待时间。
 *
 */
public class T_17_BlockingQueue {
    public static void main(String[] args) {


//        ArrayBlockingQueue<String> abq = new ArrayBlockingQueue<>(10);
        LinkedBlockingQueue<String> abq = new LinkedBlockingQueue<>(10);
        new Thread(new Producer(abq)).start();
        new Thread(new Consumer(abq)).start();

    }

    public static class Producer implements Runnable {
        private final BlockingQueue<String> mBq;

        public Producer(BlockingQueue<String> bq) {
            mBq = bq;
        }

        @Override
        public void run() {
            for (int i = 0; i <100; i++) {
                try {
                    Thread.sleep(2000);
                    mBq.put("item:" + i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static class Consumer implements Runnable {
        private BlockingQueue<String> mBq;

        public Consumer(BlockingQueue<String> bq) {
            mBq = bq;
        }

        @Override
        public void run() {
            for (int i = 0; i <100; i++) {
                try {
                    String value = mBq.take();
                    System.out.println("value :" + value);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
