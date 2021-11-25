package com.atmashiping.tank.chainofresponsibility;

import com.atmashiping.tank.AbstractGameObject;
import com.atmashiping.tank.Bullet;
import com.atmashiping.tank.Tank;
import com.atmashiping.tank.Wall;

public class TankWallCollider implements Collider {
    @Override
    public boolean collide(AbstractGameObject ago1, AbstractGameObject ago2) {
        if (ago1 instanceof Tank && ago2 instanceof Wall){
            Tank t = (Tank) ago1;
            Wall w = (Wall) ago2;
            if (t.isLive()){
                if (t.getRect().intersects(w.getRect())){
                    t.back();
                }
            }
        }else if (ago1 instanceof Wall && ago2 instanceof Tank){
            collide(ago2,ago1);
        }
        return true;
    }
}
