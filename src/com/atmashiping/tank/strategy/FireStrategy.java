package com.atmashiping.tank.strategy;

import com.atmashiping.tank.Player;

import java.io.Serializable;

//策略模式 Strategy
public interface FireStrategy extends Serializable{
    public void fire(Player p);
}
