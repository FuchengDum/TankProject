package com.atmashiping.tank.strategy;

import com.atmashiping.tank.*;

public class DefaultFireStrategy implements FireStrategy{
    @Override
    public void fire(Player p) {
        int bX = p.getX() + ResourceMgr.goodTankU.getWidth()/2 - ResourceMgr.bulletU.getWidth()/2;
        int bY = p.getY() + ResourceMgr.goodTankU.getHeight()/2 - ResourceMgr.bulletU.getHeight()/2;

//        Dir[] dirs = Dir.values();
//
//        for(Dir d:dirs)
            TankFrame.INSTANCE.getGm().add(new Bullet(bX, bY, p.getDir(), p.getGroup()));
    }
}
