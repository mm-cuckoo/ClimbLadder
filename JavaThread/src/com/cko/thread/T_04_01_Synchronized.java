package com.cko.thread;

/**
 * 传统的线程互斥
 */
public class T_04_01_Synchronized {
    public static void main(String[] args) {
        new T_04_01_Synchronized().init();
    }

    public void init() {

        /**
         * 未进行线程互斥处理
         */
//        threadFactory(new OutPuter(),true);

        /**
         * 使用this 对代码块进行互斥处理
         */
//        threadFactory(new OutPuter1(),false);
        /**
         * 同一对象中两个方法的执行内容互斥
         */
//        threadFactory(new OutPuter1(),true);
        /**
         * 同一对象中通过变量实现互斥
         */
        threadFactory(new OutPutter2(),false);
    }

    class OutPutter implements OutPutterBase {
        @Override
        public void output(String str){
            char[] charStr = str.toCharArray();
            for(Character c : charStr){
                System.out.print(c);
            }
            System.out.println();
        }

        @Override
        public void output1(String str) {
        }
    }

    /**
     * synchronized (this) this 要指向同一对象才可以对该对象进行互斥处理
     */
    class OutPutter1 implements OutPutterBase {
        @Override
        public void output(String str){
            /**
             * synchronized(this) this 指向该类实例的对象
             */
            synchronized (this){
                char[] charStr = str.toCharArray();
                for(Character c : charStr){
                    System.out.print(c);
                }
                System.out.println();
            }
        }

        /**
         * synchronized 在方法上，也是指向该方法的对象
         * @param str
         */
        @Override
        public synchronized void output1(String str){
            char[] charStr = str.toCharArray();
            for(Character c : charStr){
                System.out.print(c);
            }
            System.out.println();
        }
    }


    /**
     * 在同一个实例对象中使用同一个变量可实现互斥，不同变量是不能实现互斥的
     */
    class OutPutter2 implements OutPutterBase {

        private String sign = "XXXX";
        @Override
        public void output(String str){
            /**
             * synchronized(sign) sign 指向该类实例对象的变量,如果再起一个线程执行 下面 output1 方法，将不会起到互斥功能
             */
            synchronized (sign){
                char[] charStr = str.toCharArray();
                for(Character c : charStr){
                    System.out.print(c);
                }
                System.out.println();
            }
        }

        /**
         * synchronized 在方法上，也是指向该方法的对象
         * @param str
         */
        @Override
        public void output1(String str){
            char[] charStr = str.toCharArray();
            for(Character c : charStr){
                System.out.print(c);
            }
            System.out.println();
        }
    }

    /**
     * synchronized (this) this 要指向同一对象才可以对该对象进行互斥处理
     */
    static class OutPuter3{
        public void outputs(String str){
            /**
             * synchronized(this) 在静态累中不可以使用 this 实现互斥，要使用 className.class（类的字简码） 实现互斥
             */
            synchronized (OutPuter3.class){
                char[] charStr = str.toCharArray();
                for(Character c : charStr){
                    System.out.print(c);
                }
                System.out.println();
                System.out.println();
            }
        }

        /**
         * synchronized 在方法上，也是指向该方法的对象
         * @param str
         */
        public static synchronized void outputs1(String str){
            char[] charStr = str.toCharArray();
            for(Character c : charStr){
                System.out.print(c);
            }
            System.out.println();
        }
    }

    /**
     * 创建线程
     * @param outPuter
     */
    public void threadFactory(OutPutterBase outPuter, boolean isTwoOutPutMethod){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(500);
                        outPuter.output("aaaaaaaaaaaaaaaaaaaaaaaaa");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(500);
                        if(isTwoOutPutMethod){
                            outPuter.output1("bbbbbbbbbbbbbbbbbbbbbbbbbbb");
                        }else {
                            outPuter.output("bbbbbbbbbbbbbbbbbbbbbbbbbbb");
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    interface OutPutterBase {
        void output(String str);
        void output1(String str);
    }
}
