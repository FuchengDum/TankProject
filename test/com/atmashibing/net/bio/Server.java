package com.atmashibing.net.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        //BIO BlockingIO  并发较少的特定情况下使用
        ServerSocket ss = new ServerSocket();
        ss.bind(new InetSocketAddress("localhost",8888));
        //System.out.println("hello");
        boolean started = true;
        while (started){
            Socket accept = ss.accept();

            new Thread(() ->{
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(accept.getInputStream()));
                    String str = br.readLine();
                    System.out.println(str);
                    br.close();
                    accept.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }).start();

        }

        ss.close();
    }
}
