package com.atmashibing.net.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class Server {
    public static void main(String[] args) {
        //NIO Non-BlockingIO ByteBuffer（single pointer）  Netty 封装了NIO ByteBuffer（read write pointer)
        // AIO-Asynchronous IO
        try {
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.socket().bind(new InetSocketAddress("localhost",8888));
            ssc.configureBlocking(false);//设置为非阻塞

            System.out.println("server started listening" + ssc.getLocalAddress());

            Selector selector = Selector.open();
            ssc.register(selector, SelectionKey.OP_ACCEPT);

            while (true){
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();
                while (it.hasNext()){
                    SelectionKey key = it.next();
                    it.remove();
                    handle(key);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void handle(SelectionKey key) {
        if (key.isAcceptable()){
            try {
                //新建一个通道服务对应的客户端
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                SocketChannel sc = ssc.accept();
                ssc.configureBlocking(false);

                ssc.register(key.selector(),SelectionKey.OP_READ);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (key.isReadable()){
            SocketChannel sc =null;

            try {
                sc = (SocketChannel) key.channel();
                ByteBuffer buffer = ByteBuffer.allocate(512);
                buffer.clear();
                int len = sc.read(buffer);

                if (len != -1){
                    System.out.println(new String(buffer.array(),0,len));
                }

                ByteBuffer bufferToWrite = ByteBuffer.wrap("HelloClient".getBytes());
                sc.write(bufferToWrite);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (sc != null){
                    try {
                        sc.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }
}
