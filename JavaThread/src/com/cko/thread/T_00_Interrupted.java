package com.cko.thread;

public class T_00_Interrupted {
    public static void main(String[] args) {
        T_00_Interrupted thread = new T_00_Interrupted();
        thread.runTread();
    }

    public void runTread(){
       Thread thread =  new Thread() {

           @Override
           public void run() {
               while (true) {
                   try {
                       Thread.sleep(2000);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   System.out.println("isInterrupted:" + this.isInterrupted());
                   System.out.println("interrupted:" + Thread.interrupted());
               }
           }
       };

       thread.start();
        try {

            Thread.sleep(2000);
            System.out.println("------------------:");
            thread.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
