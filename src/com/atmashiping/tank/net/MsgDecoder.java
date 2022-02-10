package com.atmashiping.tank.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MsgDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> list) throws Exception {
        if (buf.readableBytes() < 8) return;

        MsgType msgType = MsgType.values()[buf.readInt()];

        int length = buf.readInt();

        //确认消息已读全
        if (buf.readableBytes() < length){
            buf.resetReaderIndex();
            return;
        }

        byte[] bytes = new byte[length];

        buf.readBytes(bytes);

        //反射新建类
        Msg msg = null;
        msg = (Msg) Class.forName("com.atmashiping.tank.net." + msgType.toString() + "Msg")
                   .getDeclaredConstructor().newInstance();

        msg.parse(bytes);

        /*switch (msgType){
            case TankJoin:
                msg = new TankJoinMsg();
                msg.parse(bytes);
                break;
            case TankStartMoving:
                msg = new TankMoveOrDirChangeMsg();
                msg.parse(bytes);
                break;
        }*/


        list.add(msg);
    }
}
