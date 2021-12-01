package com.atmashiping.tank.chainofresponsibility;

import com.atmashiping.tank.AbstractGameObject;

import java.io.Serializable;

public interface Collider extends Serializable{
    //return true chain go on,else chain break
    public boolean collide(AbstractGameObject ago1,AbstractGameObject ago2);
}
