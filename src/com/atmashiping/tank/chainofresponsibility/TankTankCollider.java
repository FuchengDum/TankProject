package com.atmashiping.tank.chainofresponsibility;

import com.atmashiping.tank.AbstractGameObject;
import com.atmashiping.tank.Bullet;
import com.atmashiping.tank.Tank;
import com.atmashiping.tank.Wall;

public class TankTankCollider implements Collider {
    @Override
    public boolean collide(AbstractGameObject ago1, AbstractGameObject ago2) {
        if (ago1 != ago2 && ago1 instanceof Tank && ago2 instanceof Tank){
            Tank t = (Tank) ago1;
            Tank t1 = (Tank) ago2;
            if (t.isLive() && t1.isLive()){
                if (t.getRect().intersects(t1.getRect())){
                    t.back();
                    t1.back();
                }
            }
        }
        return true;
    }
}
