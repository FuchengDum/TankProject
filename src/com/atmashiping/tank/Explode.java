package com.atmashiping.tank;

import java.awt.*;

public class Explode extends AbstractGameObject{
    private int x,y;
    private boolean live = true;
    private int width,height;
    private int step = 0;

    public Explode(int x, int y){
        this.x = x;
        this.y = y;

        this.width = ResourceMgr.explodes[0].getWidth();
        this.height = ResourceMgr.explodes[0].getHeight();

        new Thread(()->new Audio("audio/explode.wav").play()).start();
    }

    public void paint(Graphics g) {
            if (!isLive()) return;

            g.drawImage(ResourceMgr.explodes[step],x,y,null);
            step++;
            if (step >= ResourceMgr.explodes.length){
                this.die();
            }


    }

    private void die() {
        this.live = false;
    }


    public void setLive(boolean live) {
        this.live = live;
    }

    public boolean isLive(){
        return this.live;
    }
}
