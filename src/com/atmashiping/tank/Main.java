package com.atmashiping.tank;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        TankFrame f = TankFrame.INSTANCE;

        f.setVisible(true);
        //new Thread(()->new Audio("audio/war1.wav").loop()).start();


        for (;;){
            try{
                Thread.sleep(25);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
                f.repaint();
        }
    }
}
