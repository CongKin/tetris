/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

/**
 *
 * @author User
 */
public class Title extends JPanel implements KeyListener{

    private static final long serialVersionUID = 1L;
    private BufferedImage instructions;
    private WindowGame window;
    private BufferedImage[] playButton = new BufferedImage[2];
    private Timer timer;
    private JTextField linkInput;
    private JButton submit;
    private InetAddress ipAddress;
    private Integer port;
    private JLabel status;
    private Client client;
    
    public Title(WindowGame window){
        //instructions = ImageLoader.loadImage("");
        timer = new Timer(1000/60, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //repaint();
            }
        });
        
        timer.start();
        this.window = window;
        
        status = new JLabel("Haven't connect to server");
        linkInput = new JTextField("");
        submit = new JButton("Connect");
        submit.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){ 
                
                String link = linkInput.getText();
                try {
                    URI uri = new URI(link);
                    String host = uri.getHost();
                    int port = uri.getPort();
                    System.out.println("Host: " + host);
                    System.out.println("Port: " + port);
                    client = new Client(host, port);
                    client.start();
                    String clientHost = client.getIPAdress().toString();
                    String message = clientHost + " connected";
                    System.out.println(message);
                    client.writeMessages(message);
                    status.setText("Connected to Server Successfully");
                    
                    boolean start = false;
                    
                }catch(URISyntaxException e1) {
                    e1.printStackTrace();
                } 

            }
        });
        
    }
    
    public Client getClient(){
        return client;
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        g.setColor(Color.black);
        
        g.fillRect(0, 0, WindowGame.WIDTH, WindowGame.HEIGHT);
        
        //g.drawImage(instructions, WindowGame.WIDTH/2 - instructions.getWidth()/2,
                //30 - instructions.getHeight()/2 + 150, null);
        
        g.setColor(Color.white);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        g.drawString("Server IP: ", 50, 180);
        linkInput.setBounds(180,150, 300,45);
        this.add(linkInput);
        
        status.setBounds(250,370, 300,45);
        this.add(status);
                
        submit.setBounds(250,270, 95,30);
        this.add(submit);
        
        
        
    }
    
    
    @Override
    public void keyTyped(KeyEvent e) {
        /*
        if(e.getKeyChar() == KeyEvent.VK_SPACE){
            window.startTetris();
        }*/
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
    
}
