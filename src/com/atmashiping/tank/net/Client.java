package com.atmashiping.tank.net;

import com.atmashiping.tank.TankFrame;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
    public static final Client INSTANCE = new Client();

    private Client(){

    }

    private Channel channel = null;
    public void connect()  {
        EventLoopGroup wokerGroup = new NioEventLoopGroup(1);
        Bootstrap b = new Bootstrap();
        try {
            b.group(wokerGroup);
            b.channel(NioSocketChannel.class);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    channel = socketChannel;
                    socketChannel.pipeline()
                            //在管道上 Handler之前加一个Encoder
                            .addLast(new MsgEncoder())
                            .addLast(new MsgDecoder())
                            .addLast(new Client.MyHandler());
                }
            });

            ChannelFuture future = b.connect("localhost", 8888).sync();

            System.out.println("connect to server");
//        System.in.read();
            future.channel().closeFuture().sync();

            System.out.println("go on");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            wokerGroup.shutdownGracefully();

        }
    }

    public void send(TankJoinMsg tjm) {
        channel.writeAndFlush(tjm);
    }

    public void closeConnection() {
        //this.send("__byte__");
        channel.close();
    }

    static class MyHandler extends SimpleChannelInboundHandler<TankJoinMsg> {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            TankJoinMsg tankMsg = new TankJoinMsg(TankFrame.INSTANCE.getGm().getMytank());
            ctx.writeAndFlush(tankMsg);
        }

        /*@Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            //byte buf 仅仅是在jvm 存储一个引用 指向操作系统。
            //如何解决垃圾回收，Reference Count 算法
            ctx.channel().pipeline().addLast(new TankMsgDecoder());
            TankMsg tm = (TankMsg) msg;
            ClientFrame.INSTANCE.updateText(tm.toString());
            *//*ByteBuf buf = null;
            try{
                buf = (ByteBuf) msg;
                byte[] bytes = new byte[buf.readableBytes()];
                buf.getBytes(buf.readerIndex(),bytes);
                String str = new String(bytes);

                ClientFrame.INSTANCE.updateText(str);
                System.out.println(str);
                //refCnt记录引用个数
                System.out.println(((ByteBuf)msg).refCnt());
            }finally {
                if (buf != null)
                    ReferenceCountUtil.release(buf);
            }*//*


        }*/

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, TankJoinMsg msg) throws Exception {
            System.out.println(msg);
            msg.handle();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }

    }

    public static void main(String[] args) {
        Client client = new Client();
        client.connect();
    }
}
