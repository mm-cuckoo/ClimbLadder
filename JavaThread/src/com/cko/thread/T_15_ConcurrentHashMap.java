package com.cko.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

public class T_15_ConcurrentHashMap {

    interface IPolicy{
        void addCount(String key);
        void print();
    }

    /**
     * 纯在线程安全的方式
     */
    public static class BaseMapPolicy implements IPolicy {
        private final HashMap<String, Integer> hashMap = new HashMap<>();
        @Override
        public void addCount(String key) {
            int count = hashMap.getOrDefault(key, 0);
            count++;
            hashMap.put(key, count);
        }

        @Override
        public void print() {
            for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
                System.out.println("key:" + entry.getKey()  + "  value:" + entry.getValue());
            }
        }
    }

    /**
     * 这种方式可以控制达到想要的结果，但实现要注意
     */
    public static class AtomicAndBaseMapPolicy implements IPolicy {
        private final HashMap<String, AtomicInteger> hashMap = new HashMap<>();
        @Override
        public void addCount(String key) {
            AtomicInteger atomicInteger;
            if ((atomicInteger = hashMap.get(key))== null) {
                synchronized (this) {
                    /*
                     * 创建新的 AtomicInteger 对象时需要时原子的
                     */
                    if ((atomicInteger = hashMap.get(key))== null) {
                        atomicInteger = new AtomicInteger(0);
                        hashMap.put(key, atomicInteger);
                    }
                }
            }

            atomicInteger.incrementAndGet();
        }

        @Override
        public void print() {
            for (Map.Entry<String, AtomicInteger> entry : hashMap.entrySet()) {
                System.out.println("key:" + entry.getKey()  + "  value:" + entry.getValue().get());
            }
        }
    }

    public static class ConcurrentHashMapPolicy implements IPolicy {
        private final ConcurrentHashMap<String, Integer> hashMap = new ConcurrentHashMap<>();
        @Override
        public void addCount(String key) {
            hashMap.compute(key, new BiFunction<String, Integer, Integer>() {
                @Override
                public Integer apply(String s, Integer integer) {
                    return integer == null ? 1 : integer + 1;
                }
            });
        }

        @Override
        public void print() {
            for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
                System.out.println("key:" + entry.getKey()  + "  value:" + entry.getValue());
            }
        }
    }

    public static class ConcurrentHashMapAndAtomicIntegerPolicy implements IPolicy {
        private final ConcurrentHashMap<String, AtomicInteger> hashMap = new ConcurrentHashMap<>();
        @Override
        public void addCount(String key) {
            AtomicInteger atomicInteger = new AtomicInteger(0);
            AtomicInteger newatomicInteger = hashMap.putIfAbsent(key, atomicInteger);
            if (newatomicInteger != null) {
                atomicInteger = newatomicInteger;
            }
            atomicInteger.incrementAndGet();
        }

        @Override
        public void print() {

        }
    }

    private final ConcurrentHashMap<String, String> mConHashMap = new ConcurrentHashMap<>();

    /**
     * putIfAbsent(key, value):
     * 如果指定的键尚未与某个值关联，则将其与给定值关联。返回 null
     * 如果指定的键已经与某个值关联，则返回第一次存储的值
     */
    public void putIfAbsent() {
        String str1 = mConHashMap.putIfAbsent("a", "b");
        String str2 = mConHashMap.putIfAbsent("a", "c");
        String str3 = mConHashMap.putIfAbsent("a", "d");

        System.out.println("a:" + mConHashMap.get("a") + "  str1:" + str1 + "  str2:" + str2 + "  str3:" + str3);
    }


    public void runThread() {

//        IPolicy policy = new BaseMapPolicy();
//        IPolicy policy = new AtomicAndBaseMapPolicy();
        IPolicy policy = new ConcurrentHashMapPolicy();
//        IPolicy policy = new ConcurrentHashMapAndAtomicIntegerPolicy();

        Thread[] threads = new Thread[20];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread() {
                @Override
                public void run() {
                    String key = "key-1";

                    for (int j = 0; j <100; j++) {
                        policy.addCount(key);
                    }

                }
            };
            threads[i].start();
        }

        for (int i = 10; i < 20; i++) {
            threads[i] = new Thread() {
                @Override
                public void run() {
                    String key =  "key-2";

                    for (int j = 0; j <100; j++) {
                        policy.addCount(key);
                    }

                }
            };
            threads[i].start();
        }

        for (int i = 0; i < 20; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        policy.print();
    }


    public static void main(String[] args) {
        T_15_ConcurrentHashMap concurrentHashMap = new T_15_ConcurrentHashMap();
//        concurrentHashMap.putIfAbsent();
        concurrentHashMap.runThread();

        System.out.println("run end -->>>>>");
    }
}
