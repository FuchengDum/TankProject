package com.atmashiping.tank;

import com.atmashiping.tank.chainofresponsibility.ColliderChain;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class GameModel implements Serializable{
    private Player mytank;

    List<AbstractGameObject> objects ;

    //List<Collider> colliders;

    Random r = new Random();

    ColliderChain chain = new ColliderChain();

    public GameModel() {
        initGameObjects();
    }

    private void initGameObjects() {
        mytank = new Player(50 + r.nextInt(600),50+r.nextInt(400),
                Dir.values()[r.nextInt(Dir.values().length)],
                Group.values()[r.nextInt(Group.values().length)]);


        objects = new ArrayList<>();

        int tankCount = Integer.parseInt(PropertyMgr.get("initTankCount"));
        for (int i = 0; i < tankCount; i++) {
            this.add(new Tank(100 + 200*i,100,Dir.D,Group.BAD));
        }

        this.add(new Wall(300,200,200,50));
    }

    public void add(AbstractGameObject AGO){
        objects.add(AGO);
    }

    public void paint(Graphics g){

        Color c = g.getColor();
        g.setColor(Color.WHITE);
//        g.drawString("BULLETS:"+ bullets.size(),10,50);
//        g.drawString("Enemys:"+ enermys.size(),10,70);
//        g.drawString("explode:"+ enermys.size(),10,90);

        g.setColor(c);
        //e.paint(g);

        mytank.paint(g);
        for (int i = 0; i < objects.size(); i++) {
            AbstractGameObject object = objects.get(i);

            if (!object.isLive()){
                objects.remove(i);
                break;
            }
        }

        for (int i = 0; i < objects.size(); i++) {
            AbstractGameObject ago = objects.get(i);

            //去除死亡的子弹 不能在循环内，会出现子弹闪烁
            /*if (!ago.isLive()){
                objects.remove(i);
                break;
            }*/

            for (int j = 0; j < objects.size(); j++) {
                AbstractGameObject ago2 = objects.get(j);
//                    collider.collide(ago,ago2);
//                    collider2.collide(ago,ago2);
                chain.collide(ago,ago2);
            }
            if (objects.get(i).isLive()){
                objects.get(i).paint(g);
            }

        }
    }

    public Player getMytank() {
        return mytank;
    }

    public Tank findByUUID(UUID id) {
        for (AbstractGameObject o : objects){
            if (o instanceof  Tank){
                Tank t = (Tank) o;
                if (id.equals(t.getId())) return t;
            }
        }
        return null;
    }
}
