package com.atmashiping.tank.net;

import com.atmashiping.tank.*;

import java.io.*;
import java.util.UUID;

public class TankJoinMsg {
    private int x,y;
    private Dir dir;
    private boolean moving;
    private Group group;

    private UUID id;

    public TankJoinMsg() {
    }

    public TankJoinMsg(Player p){
        this.x = p.getX();
        this.y = p.getY();
        this.dir = p.getDir();
        this.moving = p.isMoving();
        this.group = p.getGroup();
        this.id =  p.getId();
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Dir getDir() {
        return dir;
    }

    public boolean isMoving() {
        return moving;
    }

    public Group getGroup() {
        return group;
    }

    public UUID getId() {
        return id;
    }

    public byte[] toBytes(){
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        byte[] bytes = null;


        try {
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);
            dos.writeInt(x);
            dos.writeInt(y);
            dos.writeInt(dir.ordinal());
            dos.writeBoolean(moving);
            dos.writeInt(group.ordinal());

            dos.writeLong(id.getMostSignificantBits());
            dos.writeLong(id.getLeastSignificantBits());
            dos.flush();

            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (baos!=null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (dos!=null)
                    dos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }

    public void parse(byte[] bytes) {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
        try{
            this.x = dis.readInt();
            this.y = dis.readInt();
            this.dir = dir.values()[dis.readInt()];
            this.moving = dis.readBoolean();
            this.group = group.values()[dis.readInt()];
            this.id = new UUID(dis.readLong(),dis.readLong());

        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                dis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return "TankJoinMsg{" +
                "x=" + x +
                ", y=" + y +
                ", dir=" + dir +
                ", moving=" + moving +
                ", group=" + group +
                ", id=" + id +
                '}';
    }

    public void handle() {
        if(this.id.equals(TankFrame.INSTANCE.getGm().getMytank().getId())) return;

        if (TankFrame.INSTANCE.getGm().findByUUID(this.id) != null) return;

        Tank tank = new Tank(this);

        TankFrame.INSTANCE.getGm().add(tank);

        Client.INSTANCE.send(new TankJoinMsg(TankFrame.INSTANCE.getGm().getMytank()));
    }
}
