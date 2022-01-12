package com.atmashiping.tank;

import com.atmashiping.tank.net.Client;

public class Main {
    public static void main(String[] args) {
        TankFrame f = TankFrame.INSTANCE;

        f.setVisible(true);
        //new Thread(()->new Audio("audio/war1.wav").loop()).start();

        new Thread(()->{
            for (;;){
                try{
                    Thread.sleep(25);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                f.repaint();
            }
        }).start();

        Client.INSTANCE.connect();
    }
}
