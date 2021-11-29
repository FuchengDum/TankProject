package com.atmashiping.tank;

import com.atmashiping.tank.chainofresponsibility.BulletTankCollider;
import com.atmashiping.tank.chainofresponsibility.BulletWallCollider;
import com.atmashiping.tank.chainofresponsibility.Collider;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TankFrame extends Frame {
    public static final TankFrame INSTANCE = new TankFrame();


    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 600;

    private GameModel gm = new GameModel();
    private TankFrame(){
        this.setTitle("tank war");
        this.setLocation(400,100);
        this.setSize(800,600);
        this.addKeyListener(new TankKeyListener());

        //initGameObjects();

        //initColliders();
    }

//    private void initColliders() {
//        colliders = new ArrayList<>();
//        String[] colliderNames = PropertyMgr.get("colliders").split(",");
//        try {
//            for (String name :colliderNames){
//                Class clazz = Class.forName("com.atmashiping.tank.chainofresponsibility." + name);
//                Collider c = (Collider) clazz.getDeclaredConstructor().newInstance();
//                colliders.add(c);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }





//    public void add(Bullet bullet){
//        this.bullets.add(bullet);
//    }

    Collider collider = new BulletTankCollider();
    Collider collider2 = new BulletWallCollider();

    @Override
    public void paint(Graphics g){

        gm.paint(g);

//        for (int i = 0; i < enermys.size(); i++) {
//            if (!enermys.get(i).isLive()){
//                enermys.remove(i);
//            }else {
//                enermys.get(i).paint(g);
//            }
//        }
//
//        for (int i = 0; i <bullets.size() ; i++) {
//            Bullet bullet = bullets.get(i);
//
//            for (int j = 0; j < enermys.size(); j++) {
//                bullet.collidesWithTank(enermys.get(j));
//
//            }
//            if (!bullet.isLive()){
//                bullets.remove(i);
//            }else {
//                bullets.get(i).paint(g);
//            }
//        }
//
//        for (int i = 0; i < explodes.size(); i++) {
//            if (!explodes.get(i).isLive()){
//                explodes.remove(i);
//            }else {
//                explodes.get(i).paint(g);
//            }
//        }
    }

    Image offScreenImage = null;

    //双缓冲：可以认为是图片的预处理，先将图片在内存中画完再推送至显存
    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
        }
        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.BLACK);
        gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        gOffScreen.setColor(c);
        paint(gOffScreen);
        g.drawImage(offScreenImage, 0, 0, null);
    }

//    public void add(Explode explode) {
//        this.explodes.add(explode);
//    }

    //Observer模式
    private class TankKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            gm.getMytank().keyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            gm.getMytank().keyReleased(e);
        }
    }

    public GameModel getGm() {
        return gm;
    }
}
