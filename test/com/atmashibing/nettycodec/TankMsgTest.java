package com.atmashibing.nettycodec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class TankMsgTest {
    @Test
    void decode(){
        EmbeddedChannel ec = new EmbeddedChannel();

        ec.pipeline().addLast(new TankMsgDecoder());

        ByteBuf bf = Unpooled.buffer();
        bf.writeInt(5);
        bf.writeInt(8);

        ec.writeInbound(bf);

        TankMsg tm = ec.readInbound();

        assertEquals(5,tm.x);
        assertEquals(8,tm.y);
    }
    
    @Test
    void encode(){
        EmbeddedChannel ec = new EmbeddedChannel();

        ec.pipeline().addLast(new TankMsgEncoder());

        TankMsg tm = new TankMsg(5,8);

        ec.writeOutbound(tm);

        ByteBuf bf = ec.readOutbound();

        int x = bf.readInt();
        int y = bf.readInt();

        assertEquals(5,x);
        assertEquals(8,y);
    }
}