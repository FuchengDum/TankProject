package com.atmashiping.tank;

import java.awt.*;
import java.io.Serializable;

public abstract class AbstractGameObject implements Serializable{
    public abstract void paint(Graphics p);

    public abstract boolean isLive();
}
