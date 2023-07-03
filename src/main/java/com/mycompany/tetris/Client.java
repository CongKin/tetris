/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tetris;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *
 * @author Cheh Shu Ze
 */
public class Client implements Runnable{
    private String host;
    private int port;
    
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean running = false;
    
    public Client(String host, int port){
        this.host = host;
        this.port = port;
    }
    
    //connect to server
    public void connect(){
        try{
            socket = new Socket(host,port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            //listener = new EventListener();
            new Thread(this).start();
            //createAndShowGUI(); // Create the GUI
        }catch(ConnectException e){
            System.out.println("Unable to connect to the server.");
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    //close connection
    public void close(){
        try{
            running = false;
            //RemovePlayerPacket packet = new RemovePlayerPacket();
            //sendObject(packet);
            in.close();
            out.close();
            socket.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    //send data to server
    public void sendObject(Object packet){
        try{
            out.writeObject(packet);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
       try{
           running = true;
           
           while(running){
               try{
                   Object data = in.readObject();
                   //updateGUI(data);
                   //listener.received(data);
               }catch(ClassNotFoundException e){
                    e.printStackTrace();
               }catch(SocketException e){
                   close();
               }
           }
       }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public InetAddress getIPAdress(){
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            System.out.println("Server IP Address: " + localhost.getHostAddress());
            return localhost;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
}
