package com.atmashiping.tank;

import com.atmashiping.tank.net.Client;
import com.atmashiping.tank.net.TankMoveOrDirChangeMsg;
import com.atmashiping.tank.net.TankStopMsg;
import com.atmashiping.tank.strategy.FireStrategy;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.UUID;

public class Player extends AbstractGameObject {
    public static final int SPEED = 5;
    private int x, y;
    private Dir dir;
    private boolean bL, bR, bU, bD;
    private boolean moving;
    private Group group;
    private boolean live = true;

    private UUID id = UUID.randomUUID();

    //传递TankFrame的引用;使用单例模式
    //TankFrame tf;
    private FireStrategy strategy = null;

    public Player(int x, int y, Dir dir, Group group) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        //init fire strategy
        this.initFireStrategy();
    }



    public void paint(Graphics g) {
        if (!this.isLive()) return;

        Color color = g.getColor();
        g.setColor(Color.yellow);
        g.drawString(id.toString(),x,y-10);
        g.setColor(color);


        switch (dir) {
            case L:
                g.drawImage(this.group.equals(Group.BAD)?ResourceMgr.badTankL:ResourceMgr.goodTankL,x,y,null);
                break;
            case U:
                g.drawImage(this.group.equals(Group.BAD)?ResourceMgr.badTankU:ResourceMgr.goodTankU,x,y,null);
                break;
            case R:
                g.drawImage(this.group.equals(Group.BAD)?ResourceMgr.badTankR:ResourceMgr.goodTankR,x,y,null);
                break;
            case D:
                g.drawImage(this.group.equals(Group.BAD)?ResourceMgr.badTankD:ResourceMgr.goodTankD,x,y,null);
                break;
        }

        move();
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT:
                bL = true;
                break;
            case KeyEvent.VK_RIGHT:
                bR = true;
                break;
            case KeyEvent.VK_UP:
                bU = true;
                break;
            case KeyEvent.VK_DOWN:
                bD = true;
                break;
        }
        setMainDir();
    }

    private void setMainDir() {
        boolean oldMoving = moving;

        Dir oldDir = dir;

        if (!bL && !bD && !bR && !bU){
            moving = false;
            //send stop msg
            Client.INSTANCE.send(new TankStopMsg(this.id, this.x, this.y));

        }

        else {
            moving = true;
            if (bL && !bD && !bR && !bU)
                dir = Dir.L;

            if (!bL && bD && !bR && !bU)
                dir = Dir.D;

            if (!bL && !bD && bR && !bU)
                dir = Dir.R;

            if (!bL && !bD && !bR && bU)
                dir = Dir.U;

            //old status is not moving
            if (!oldMoving)
                //send moving msg
                Client.INSTANCE.send(new TankMoveOrDirChangeMsg(this.id, this.x, this.y, this.dir));

            if (!this.dir.equals(oldDir))
                Client.INSTANCE.send(new TankMoveOrDirChangeMsg(this.id, this.x, this.y, this.dir));

        }

    }

    private void move() {
        if (!moving) return;
        switch (dir) {
            case L:
                x -= SPEED;
                break;
            case R:
                x += SPEED;
                break;
            case U:
                y -= SPEED;
                break;
            case D:
                y += SPEED;
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT:
                bL = false;
                break;
            case KeyEvent.VK_RIGHT:
                bR = false;
                break;
            case KeyEvent.VK_UP:
                bU = false;
                break;
            case KeyEvent.VK_DOWN:
                bD = false;
                break;
            case KeyEvent.VK_CONTROL:
                fire();
                break;
        }
        setMainDir();
    }

    private void initFireStrategy() {
        String className = PropertyMgr.get("tankFireStrategy");
        try {
            Class clazz = Class.forName("com.atmashiping.tank.strategy." + className);
            strategy = (FireStrategy) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fire() {

        //ClassLoader loader = Player.class.getClassLoader();


        //FireStrategy strategy = new LeftRightFireStrategy();
        strategy.fire(this);

    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void die() {
        this.setLive(false);
        TankFrame.INSTANCE.getGm().add(new Explode(x,y));
    }

    public boolean isLive() {
        return this.live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public Group getGroup() {
        return group;
    }

    public Dir getDir() {
        return dir;
    }

    public boolean isMoving(){
        return this.moving;
    }

    public UUID getId(){
        return this.id;
    }
}
