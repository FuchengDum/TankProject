package com.atmashiping.tank.chainofresponsibility;

import com.atmashiping.tank.AbstractGameObject;
import com.atmashiping.tank.Bullet;
import com.atmashiping.tank.Wall;

public class BulletWallCollider implements Collider {
    @Override
    public boolean collide(AbstractGameObject ago1, AbstractGameObject ago2) {
        if (ago1 instanceof Bullet && ago2 instanceof Wall){
            Bullet b = (Bullet) ago1;
            Wall w = (Wall) ago2;
            if (b.isLive()){
                if (b.getRect().intersects(w.getRect())){
                    b.die();
                    return false;
                }
            }
        }else if (ago1 instanceof Wall && ago2 instanceof Bullet){
            return collide(ago2,ago1);
        }
        return true;
    }
}
