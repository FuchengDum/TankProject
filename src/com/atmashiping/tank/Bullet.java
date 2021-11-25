package com.atmashiping.tank;

import java.awt.*;

public class Bullet extends AbstractGameObject{
    private int x,y;
    private Dir dir;
    private boolean live = true;
    private int w = ResourceMgr.bulletU.getWidth();
    private int h = ResourceMgr.bulletU.getHeight();
    private Rectangle rect;

    public static final int SPEED = 10;

    public Group getGroup() {
        return group;
    }

    private Group group;

    public Bullet (int x, int y,Dir dir,Group group){
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;

        rect = new Rectangle(x,y,w,h);
    }

    public void paint(Graphics g) {
            switch (dir){
                case L:
                    g.drawImage(ResourceMgr.bulletL,x,y,null);
                    break;
                case U:
                    g.drawImage(ResourceMgr.bulletU,x,y,null);
                    break;
                case R:
                    g.drawImage(ResourceMgr.bulletR,x,y,null);
                    break;
                case D:
                    g.drawImage(ResourceMgr.bulletD,x,y,null);
                    break;
            }
        move();
        //更新子弹方块的位置
        rect.x = x;
        rect.y = y;

        /*Color color1 = g.getColor();
        g.setColor(Color.RED);
        g.drawRect(rect.x,rect.y,rect.width,rect.height);
        g.setColor(color1);*/
    }

    private void move() {
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


        //边界检查
        boundsCheck();
    }

    public Rectangle getRect() {
        return rect;
    }

    public void collidesWithTank(Tank tank) {
        if (!this.isLive() || !tank.isLive()) return;
        if (this.group == tank.getGroup()) return;

//        Rectangle rect = new Rectangle(x,y,ResourceMgr.bulletU.getWidth(),
//                ResourceMgr.bulletU.getHeight());
        Rectangle rectTank = new Rectangle(tank.getX(), tank.getY(),
                ResourceMgr.goodTankU.getWidth(), ResourceMgr.goodTankU.getHeight());
        if (rect.intersects(rectTank)){
            this.die();
            tank.die();
        }
    }

    //子弹需要做个边界检查
    private void boundsCheck() {
        if (x < 0 || y < 30 || x>TankFrame.GAME_WIDTH || y>TankFrame.GAME_HEIGHT){
            live = false;
        }
    }

    public void die(){
        this.setLive(false);
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }


}
