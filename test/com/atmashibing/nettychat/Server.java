package com.atmashibing.nettychat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.SocketAddress;

public class Server {
    //保存channel
    public ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public void ServerStart () {
        //负责接客
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(2);

        //负责服务
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(4);

        try {
            //Server自动辅助类
            ServerBootstrap b = new ServerBootstrap();
            //异步全双工
            ChannelFuture future  = b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //内部处理accept的过程
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new ServerChildHandler());
                        }
                    })
                    .bind(8888)
                    .sync();

            future.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }


    class ServerChildHandler extends ChannelInboundHandlerAdapter{

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            //如果channel 准确无误，将channel 加入 管道的列表中
            clients.add(ctx.channel());
            //获取 IP地址
            SocketAddress socketAddress = ctx.channel().remoteAddress();
            System.out.println(socketAddress.toString());

        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf buf = null;

                buf = (ByteBuf) msg;
                byte[] bytes = new byte[buf.readableBytes()];
                buf.getBytes(buf.readerIndex(),bytes);
                String str = new String(bytes);
                if ("__byte__".equals(str)){
                    System.out.println("client ready to quit.");
                    clients.remove(ctx.channel());
                    ctx.close();

                    System.out.println(clients.size());
                }else {
                    //writeAndFlush 对Bytebuf自动进行释放
                    clients.writeAndFlush(buf);

                }
                //buf.release();

            //Server.clients.iterator()
//            super.channelRead(ctx, msg);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            //super.exceptionCaught(ctx, cause);
            //出异常后 将通道移除并关闭
            clients.remove(ctx.channel());
            ctx.close();
        }
    }
}



