package com.atmashiping.tank.chainofresponsibility;

import com.atmashiping.tank.AbstractGameObject;
import com.atmashiping.tank.Bullet;
import com.atmashiping.tank.ResourceMgr;
import com.atmashiping.tank.Tank;

import java.awt.*;

public class BulletTankCollider implements Collider {
    @Override
    public boolean collide(AbstractGameObject ago1, AbstractGameObject ago2) {
        if (ago1 instanceof Bullet && ago2 instanceof Tank){
            Bullet b = (Bullet) ago1;
            Tank t = (Tank) ago2;
//            b.collidesWithTank(t);
            if (!b.isLive() || !t.isLive()) return false;
            if (b.getGroup() == t.getGroup()) return true;

//        Rectangle rect = new Rectangle(x,y,ResourceMgr.bulletU.getWidth(),
//                ResourceMgr.bulletU.getHeight());
//            Rectangle rectTank = new Rectangle(t.getX(), t.getY(),
//                    ResourceMgr.goodTankU.getWidth(), ResourceMgr.goodTankU.getHeight());

            Rectangle rectTank = t.getRect();

            if (b.getRect().intersects(rectTank)){
                b.die();
                t.die();
                return false;
            }
        }else if (ago1 instanceof Tank && ago2 instanceof Bullet){
            collide(ago2,ago1);
        }

        return true;
    }
}
