package com.atmashiping.tank.strategy;

import com.atmashiping.tank.*;

public class LeftRightFireStrategy implements FireStrategy {
    @Override
    public void fire(Player p) {
        int bX = p.getX() + ResourceMgr.goodTankU.getWidth()/2 - ResourceMgr.bulletU.getWidth()/2;
        int bY = p.getY() + ResourceMgr.goodTankU.getHeight()/2 - ResourceMgr.bulletU.getHeight()/2;

        TankFrame.INSTANCE.getGm().add(new Bullet(p.getId(),bX, bY, Dir.L, p.getGroup()));
        TankFrame.INSTANCE.getGm().add(new Bullet(p.getId(),bX, bY, Dir.R, p.getGroup()));

    }
}
