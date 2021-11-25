package com.atmashiping.tank;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Wall extends AbstractGameObject{
    private int x,y,w,h;
    private Rectangle rect;

    public Wall(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        rect = new Rectangle(x,y,w,h);
    }

    public void paint(Graphics g){
        Color color = g.getColor();
        g.setColor(color.GRAY);
        g.fillRect(x,y,w,h);
        g.setColor(color);
        Color color1 = g.getColor();
        g.setColor(Color.RED);
        g.drawRect(rect.x,rect.y,rect.width,rect.height);
        g.setColor(color1);
    }

    @Override
    public boolean isLive() {
        return true;
    }

    public Rectangle getRect() {
        return rect;
    }
}
