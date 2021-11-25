package com.atmashiping.tank.chainofresponsibility;

import com.atmashiping.tank.AbstractGameObject;

public interface Collider {
    //return true chain go on,else chain break
    public boolean collide(AbstractGameObject ago1,AbstractGameObject ago2);
}
