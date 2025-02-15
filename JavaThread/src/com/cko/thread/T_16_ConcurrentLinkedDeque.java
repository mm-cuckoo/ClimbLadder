package com.cko.thread;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * add：将指定的元素插入到此队列中（如果立即可行且不会违反容量限制），在成功时返回 true，如果当前没有可用空间，则抛出 IllegalStateException。
 * offer：将指定元素插入到此队列的尾部（如果立即可行且不会超出此队列的容量），在成功时返回 true，如果此队列已满，则返回 false。
 * put：将指定元素插入到此队列的尾部，如有必要，则等待空间变得可用。
 *
 * remove：移除并返回元素，若队列为空，则抛异常。
 * poll：移除并返回元素，若队列为空，返回null。
 * take：若队列为空，发生阻塞，等待有元素。
 *
 * element：返回队列头部的元素， 如果队列为空，则抛出一个NoSuchElementException异常
 * peek：返回队列头部的元素，如果队列为空，则返回null
 *
 * 由于不是阻塞队列，所以没有实现put、take方法。并且该队列是基于链表实现的无界队列，理论上不会说没有空间。所以add和offer方法是一样的，不会抛异常
 */
public class T_16_ConcurrentLinkedDeque {
    public static void main(String[] args) {
        ConcurrentLinkedDeque<String> concurrentLinkedDeque = new ConcurrentLinkedDeque<>();
        for (int i = 0; i < 10; i++) {
            concurrentLinkedDeque.offer("i:" + i);
        }

        for (int i = 0; i < 11; i++) {
            String item = concurrentLinkedDeque.pop();
            System.out.println("item:" + item);
        }
    }
}
