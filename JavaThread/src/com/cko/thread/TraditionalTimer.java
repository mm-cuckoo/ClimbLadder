package com.cko.thread;

import java.util.Timer;
import java.util.TimerTask;

/**
 * <br/>************************************************
 * <br/>MSG : 计时器创建 ClimbLadder
 * <br/>************************************************
 */
public class TraditionalTimer {


    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //执行内容
                System.out.println("Time is run.....");
            }
        },0,1000);
    }
}
