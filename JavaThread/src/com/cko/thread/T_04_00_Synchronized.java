package com.cko.thread;

import org.openjdk.jol.info.ClassLayout;

/**
 * 锁升级验证
 *
 */
public class T_04_00_Synchronized {

    public static void main(String[] args) {
        T_04_00_Synchronized lock = new T_04_00_Synchronized();
//        lock.lockUp1();
//        lock.lockUp2();
        lock.lockUp3();


    }

    /**
     * 偏向锁，在 synchronized 中线程会由无锁升级为偏向锁
     * 未进入同步块，MarkWord 为：
     * java.lang.Object object internals:
     * OFF  SZ   TYPE DESCRIPTION               VALUE
     *   0   8        (object header: mark)     0x0000000000000005 (biasable; age: 0)
     *   8   4        (object header: class)    0x00001000
     *  12   4        (object alignment gap)
     *
     * 进入同步块，MarkWord 为：
     * java.lang.Object object internals:
     * OFF  SZ   TYPE DESCRIPTION               VALUE
     *   0   8        (object header: mark)     0x00007fe90e008805 (biased: 0x0000001ffa438022; epoch: 0; age: 0)
     *   8   4        (object header: class)    0x00001000
     *  12   4        (object alignment gap)
     */
    public void lockUp1() {
        Object o = new Object();
        System.out.println("未进入同步块，MarkWord 为：");
        System.out.println(ClassLayout.parseInstance(o).toPrintable());
        synchronized (o){
            System.out.println(("进入同步块，MarkWord 为："));
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
        }
    }

    /**
     * 如果调用Object.hashcode 会导致锁升级，在synchronized外面调用hashcode 会升级为轻量级锁，在内部调用会升级为重量级锁
     *未生成 hashcode，MarkWord 为：
     * java.lang.Object object internals:
     * OFF  SZ   TYPE DESCRIPTION               VALUE
     *   0   8        (object header: mark)     0x0000000000000005 (biasable; age: 0)
     *   8   4        (object header: class)    0x00001000
     *  12   4        (object alignment gap)
     *
     * 生成 hashcode，MarkWord 为：
     * java.lang.Object object internals:
     * OFF  SZ   TYPE DESCRIPTION               VALUE
     *   0   8        (object header: mark)     0x0000006a28ffa401 (hash: 0x6a28ffa4; age: 0)
     *   8   4        (object header: class)    0x00001000
     *  12   4        (object alignment gap)
     *
     * 进入同步块，MarkWord 为：
     * java.lang.Object object internals:
     * OFF  SZ   TYPE DESCRIPTION               VALUE
     *   0   8        (object header: mark)     0x00007000039c8a10 (thin lock: 0x00007000039c8a10)
     *   8   4        (object header: class)    0x00001000
     *  12   4        (object alignment gap)
     *
     */
    public void lockUp2() {
        Object o = new Object();
        System.out.println("未生成 hashcode，MarkWord 为：");
        System.out.println(ClassLayout.parseInstance(o).toPrintable());
        System.out.println("生成 hashcode，MarkWord 为：");
        o.hashCode();
        System.out.println(ClassLayout.parseInstance(o).toPrintable());
        synchronized (o){
            System.out.println(("进入同步块，MarkWord 为："));
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
        }

    }

    /**
     * 如果调用Object.hashcode 会导致锁升级，在synchronized在内部调用hashcode 会升级为重量级锁
     * 未生成 hashcode，MarkWord 为：
     * java.lang.Object object internals:
     * OFF  SZ   TYPE DESCRIPTION               VALUE
     *   0   8        (object header: mark)     0x0000000000000005 (biasable; age: 0)
     *   8   4        (object header: class)    0x00001000
     *  12   4        (object alignment gap)
     *
     * 生成 hashcode
     * 同一线程再次进入同步块，MarkWord 为：
     * java.lang.Object object internals:
     * OFF  SZ   TYPE DESCRIPTION               VALUE
     *   0   8        (object header: mark)     0x00007f8539613902 (fat lock: 0x00007f8539613902)
     *   8   4        (object header: class)    0x00001000
     *  12   4        (object alignment gap)
     */
    public void lockUp3() {
        Object o = new Object();
        System.out.println("未生成 hashcode，MarkWord 为：");
        System.out.println(ClassLayout.parseInstance(o).toPrintable());
        synchronized (o){
            o.hashCode();
            System.out.println("生成 hashcode");
            System.out.println(("同一线程再次进入同步块，MarkWord 为："));
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
        }
    }

}
