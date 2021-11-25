package com.atmashiping.tank;

import java.awt.*;
import java.util.Random;


public class Tank extends AbstractGameObject{
    private int x,y;
    private Dir dir;
    private boolean bL,bR,bU,bD;
    private boolean moving = true;
    private Group group;
    private int width,height;
    private Rectangle rect;

    public static final int SPEED = 5;
    private boolean live = true;

    private int oldX,oldY;
    //传递TankFrame的引用;使用单例模式
    //TankFrame tf;

    public Tank(int x ,int y,Dir dir,Group group){
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        this.oldX = x;
        this.oldY = y;
        this.width = ResourceMgr.badTankU.getWidth();
        this.height = ResourceMgr.badTankU.getHeight();
        rect = new Rectangle(x,y,width,height);
    }

    public Group getGroup(){
        return this.group;
    }

    public void paint(Graphics g) {
        if(!this.isLive()) return;

        if (this.group== Group.BAD){
            switch (dir){
                case L:
                    g.drawImage(ResourceMgr.badTankL,x,y,null);
                    break;
                case U:
                    g.drawImage(ResourceMgr.badTankU,x,y,null);
                    break;
                case R:
                    g.drawImage(ResourceMgr.badTankR,x,y,null);
                    break;
                case D:
                    g.drawImage(ResourceMgr.badTankD,x,y,null);
                    break;
            }
        }
        move();
        //update location
        rect.x = x;
        rect.y = y;
    }


    private void move(){
        if (!moving) return;
        oldX = x;
        oldY = y;

        switch (dir){
            case L:
                x -= SPEED;
                break;
            case R:
                x += SPEED;
                break;
            case U:
                y -= SPEED;
                break;
            case D:
                y += SPEED;
                break;
        }

        boundsCheck();

        randomDir();
        if (random.nextInt(100)>90)
            fire();
    }

    private Random random = new Random();

    private void randomDir() {
        if (random.nextInt(100) > 90)
            this.dir = Dir.randomDir();
    }

    //需要做个边界检查
    private void boundsCheck() {
        if (x < 0 || y < 30 || x + width >TankFrame.GAME_WIDTH || y + height>TankFrame.GAME_HEIGHT){
            this.back();
        }
    }

    public void back() {
        this.x = oldX;
        this.y = oldY;
    }

    private void stop() {
        this.moving = false;
    }

    private void fire() {
        int bX = x + ResourceMgr.goodTankU.getWidth()/2 - ResourceMgr.bulletU.getWidth()/2;
        int bY = y + ResourceMgr.goodTankU.getHeight()/2 - ResourceMgr.bulletU.getHeight()/2;
        TankFrame.INSTANCE.add(new Bullet(bX, bY, dir, group));
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public void die() {
        this.setLive(false);
        TankFrame.INSTANCE.add(new Explode(x,y));
    }

    public boolean isLive(){
        return this.live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }


    public Rectangle getRect() {
        return rect;
    }
}
