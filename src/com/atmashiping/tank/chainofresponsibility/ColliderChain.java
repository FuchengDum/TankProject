package com.atmashiping.tank.chainofresponsibility;

import com.atmashiping.tank.AbstractGameObject;
import com.atmashiping.tank.PropertyMgr;

import java.util.ArrayList;
import java.util.List;

public class ColliderChain implements Collider{
    private List<Collider> colliders;

    public ColliderChain(){
        initColliders();
    }

    private void initColliders() {
        colliders = new ArrayList<>();
        String[] colliderNames = PropertyMgr.get("colliders").split(",");
        try {
            for (String name :colliderNames){
                Class clazz = Class.forName("com.atmashiping.tank.chainofresponsibility." + name);
                Collider c = (Collider) clazz.getDeclaredConstructor().newInstance();
                colliders.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //责任链模式
    public boolean collide(AbstractGameObject ago1,AbstractGameObject ago2){
        for (Collider collider : colliders){
            if (!collider.collide(ago1,ago2))
                return false;
        }
        return true;
    }

}
