package com.atmashibing.net.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) throws Exception {
        //负责接客
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(2);

        //负责服务
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(4);

        //Server自动辅助类
        ServerBootstrap b = new ServerBootstrap();

        b.group(bossGroup, workerGroup);

        //异步全双工
        b.channel(NioServerSocketChannel.class);
        //内部处理accept的过程
        b.childHandler(new MyChildeInitalizer());
        ChannelFuture future = b.bind(8888).sync();

        future.channel().closeFuture().sync();

        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

}

    class MyChildeInitalizer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            System.out.println("a client connect !");
            socketChannel.pipeline().addLast(new MyChildHandler());
        }


    }

    class MyChildHandler extends ChannelInboundHandlerAdapter{
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf buf = (ByteBuf) msg;
            System.out.println(buf.toString());
            //buf.release();
            ctx.writeAndFlush(msg);
//            super.channelRead(ctx, msg);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            super.exceptionCaught(ctx, cause);
            ctx.close();
        }
    }


