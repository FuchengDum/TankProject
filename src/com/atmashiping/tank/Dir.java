package com.atmashiping.tank;

import java.util.Random;

public enum Dir {
    L,U,R,D;

    private static Random r = new Random();

    public static Dir randomDir(){
        return values()[r.nextInt(Dir.values().length)];
    }
}
