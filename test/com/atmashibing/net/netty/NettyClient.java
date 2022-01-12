package com.atmashibing.net.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;


public class NettyClient {
    public static void main(String[] args) throws InterruptedException, IOException {
        EventLoopGroup wokerGroup = new NioEventLoopGroup(1);
        Bootstrap b = new Bootstrap();
        b.group(wokerGroup);
        b.channel(NioSocketChannel.class);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new MyHandler());
            }
        });

        ChannelFuture future = b.connect("localhost",8888).sync();

//        System.in.read();
        future.channel().closeFuture().sync();

        System.out.println("go on");

        wokerGroup.shutdownGracefully();
    }
        static class MyHandler extends ChannelInboundHandlerAdapter {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                System.out.println(msg.toString());
                System.out.println(((ByteBuf)msg).refCnt());
            }

            @Override
            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                cause.printStackTrace();
                ctx.close();
            }

            @Override
            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                ByteBuf buf = Unpooled.copiedBuffer("mashibing".getBytes());
                ctx.writeAndFlush(buf);

            }
        }

}
