package com.atmashibing.nettychat;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ServerFrame extends Frame {
    private static final ServerFrame INSTANCE = new ServerFrame();

    TextArea taServer = new TextArea();
    TextArea taClient = new TextArea();

    private Server server = new Server();
    public ServerFrame(){
        this.setSize(800,600);
        this.setLocation(300,30);
        Panel panel = new Panel(new GridLayout(1, 2));
        panel.add(taServer);
        panel.add(taClient);
        this.add(panel);

        taServer.setFont( new Font("Helvetica",Font.PLAIN,25));
        taClient.setFont( new Font("Helvetica",Font.PLAIN,25));

        this.updateClientMsg("client:");
        this.updateServerMsg("server:");

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

    }

    public void updateServerMsg(String str){
        this.taServer.setText(taServer.getText() + str + System.getProperty("line.separator"));
    }

    public void updateClientMsg(String str){
        this.taClient.setText(taClient.getText() + str + System.getProperty("line.separator"));
    }

    public static void main(String[] args) {
        ServerFrame.INSTANCE.setVisible(true);
        ServerFrame.INSTANCE.server.ServerStart();
    }
}
