package com.atmashiping.tank.net;

import com.atmashiping.tank.Dir;
import com.atmashiping.tank.Group;
import com.atmashiping.tank.Player;
import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


class MsgEncoderTest {

    @Test
    void encode() {
        EmbeddedChannel ec = new EmbeddedChannel();

        ec.pipeline().addLast(new MsgEncoder());
        Player player = new Player(5, 8, Dir.D, Group.BAD);
        TankJoinMsg tjm = new TankJoinMsg(player);

        ec.writeOutbound(tjm);

        ByteBuf bf = ec.readOutbound();

        int length = bf.readInt();
        int x = bf.readInt();
        int y = bf.readInt();
        Dir dir = Dir.values()[bf.readInt()];
        boolean moving = bf.readBoolean();
        Group group = Group.values()[bf.readInt()];
        UUID id = new UUID(bf.readLong(),bf.readLong());

        assertEquals(33,length);
        assertEquals(5,x);
        assertEquals(8,y);
        assertEquals(Dir.D,dir);
        assertFalse(moving);
        assertEquals(Group.BAD,group);
        assertEquals(player.getId(),id);
    }
}