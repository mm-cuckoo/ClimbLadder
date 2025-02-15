package com.cko.thread;

import java.util.concurrent.SynchronousQueue;

public class T_17_1_SynchronousQueue {
    public static void main(String[] args) {
        SynchronousQueue<Integer> synchronousQueue = new SynchronousQueue<>();


        int count = 0;
        while (true) {
            count++;

            if (count == 5){
                break;
            }

            System.out.println("count :" + count);



        }

        System.out.println("====end =====");


    }
}
