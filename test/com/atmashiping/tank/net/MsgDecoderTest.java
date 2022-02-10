package com.atmashiping.tank.net;

import com.atmashiping.tank.Dir;
import com.atmashiping.tank.Group;
import com.atmashiping.tank.Player;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class MsgDecoderTest {

    @Test
    void encode() {
        EmbeddedChannel ec = new EmbeddedChannel();

        ec.pipeline().addLast(new MsgEncoder());
        Player player = new Player(5, 8, Dir.D, Group.BAD);
        TankJoinMsg tjm = new TankJoinMsg(player);

        ec.writeOutbound(tjm);

        ByteBuf bf = ec.readOutbound();

        MsgType msgType = MsgType.values()[bf.readInt()];
        int length = bf.readInt();
        int x = bf.readInt();
        int y = bf.readInt();
        Dir dir = Dir.values()[bf.readInt()];
        boolean moving = bf.readBoolean();
        Group group = Group.values()[bf.readInt()];
        UUID id = new UUID(bf.readLong(),bf.readLong());

        assertEquals(MsgType.TankJoin,msgType);
        assertEquals(33,length);
        assertEquals(5,x);
        assertEquals(8,y);
        assertEquals(Dir.D,dir);
        assertFalse(moving);
        assertEquals(Group.BAD,group);
        assertEquals(player.getId(),id);
    }

    @Test
    void decode(){
        EmbeddedChannel ec = new EmbeddedChannel();

        ec.pipeline().addLast(new MsgDecoder());

        UUID id = UUID.randomUUID();
        ByteBuf buf = Unpooled.buffer();

        buf.writeInt(MsgType.TankJoin.ordinal());
        buf.writeInt(33);
        buf.writeInt(5);
        buf.writeInt(8);
        buf.writeInt(Dir.D.ordinal());
        buf.writeBoolean(true);
        buf.writeInt(Group.GOOD.ordinal());
        buf.writeLong(id.getMostSignificantBits());
        buf.writeLong(id.getLeastSignificantBits());


        ec.writeInbound(buf);

        TankJoinMsg tjm = ec.readInbound();

        assertEquals(MsgType.TankJoin,tjm.getMsgType());
        assertEquals(5,tjm.getX());
        assertEquals(8,tjm.getY());
        assertEquals(Dir.D,tjm.getDir());
        assertTrue(tjm.isMoving());
        assertEquals(Group.GOOD,tjm.getGroup());
        assertEquals(id,tjm.getId());

    }
}