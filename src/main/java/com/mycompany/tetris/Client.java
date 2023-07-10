/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.tetris;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import packets.Message;

public class Client {

    private String serverAddress;
    private int serverPort;
    private Socket socket;
    private InputStream input;
    private OutputStream output;
    private OutputStream objectOutputStream;
    private InputStream objectInputStream;
    
    Lock objectLock = new ReentrantLock();

    public Client(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void start() {
        try {
            // Connect to the server
            socket = new Socket(serverAddress, serverPort);
//            input = socket.getInputStream();
//            output = socket.getOutputStream();
            
            System.out.println("socket");

            Message message = new Message("John", "Hello, world!");

            // Start separate threads for reading and writing messages
            
            Thread writeThread = new Thread(() -> writeMessages(message));
            Thread readThread = new Thread(this::readMessages);

            
            writeThread.start();
            writeThread.join();
            readThread.start();
            readThread.join();
            // Wait for both threads to finish
            
            
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Close the socket and streams when finished
            close();
        }
    }

    private void readMessages() {
        
        boolean isWritingObject = false;
        
        try {
//            byte[] buffer = new byte[1024];
//            int bytesRead;
            input = new NonClosingInputStream(socket.getInputStream());
            
            while (!isWritingObject) {
                // Read the signal from the server using the normal InputStream
                
                int signal = input.read();
                if (signal == 1) {
                    input.close();
                    isWritingObject = true;
                }
            }
            objectLock.lock();
            try {
                // Initialize the ObjectInputStream after receiving the signal
                objectInputStream = new NonClosingInputStream(socket.getInputStream());
                ObjectInputStream objectInput = new ObjectInputStream(objectInputStream);

                // Read the object sent by the server
                Object obj = objectInput.readObject();
                if (obj instanceof String) {
                    String message = (String) obj;
                    System.out.println("Received from server: " + message);
                }

                // Close the ObjectInputStream when done
                objectInput.close();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                objectLock.unlock();
            }
//            while ((bytesRead = input.read(buffer)) != -1) {
//                String message = new String(buffer, 0, bytesRead);
//                System.out.println("Received from server: " + message);
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private void writeMessages() {
//        try {
//            Scanner scanner = new Scanner(System.in);
//
//            while (true) {
//                System.out.print("Enter a message to send to the server (or 'exit' to quit): ");
//                String message = scanner.nextLine();
//
//                if (message.equalsIgnoreCase("exit")) {
//                    break;
//                }
//
//                output.write(message.getBytes());
//                output.flush();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private void writeMessages(Object packet) {
        System.out.println("write"); 
        ObjectOutputStream objectOutput = null;
        objectLock.lock();
        try {
            // Write the object to the ObjectOutputStream
            output = new NonClosingOutputStream(socket.getOutputStream());
            // Write the object to the ObjectOutputStream
            output.write(1);
            output.close();
            objectOutputStream = new NonClosingOutputStream(socket.getOutputStream());
            objectOutput = new ObjectOutputStream(objectOutputStream);
            objectOutput.writeObject(packet);
            objectOutput.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Release the lock in a finally block
            objectLock.unlock();
            if (objectOutput != null) {
                try {
                    objectOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void close() {
        try {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String serverAddress = "192.168.0.147";
        int serverPort = 12345;
        
        Client client = new Client(serverAddress, serverPort);
        client.start();
    }
}

/*package com.mycompany.tetris;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import packets.SignalPacket;
import sun.misc.Signal;
*/
/**
 *
 * @author Cheh Shu Ze
 */
/*
public class Client implements Runnable{
    private String host;
    private int port;
    private WindowGame window;
    
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean running = false;
    
    public Client(String host, int port, WindowGame window){
        this.host = host;
        this.port = port;
        this.window = window;
        System.out.println(host + port);
        System.out.println("client");
    }
    
    //connect to server
    public void connect(){
        try{
            System.out.println("try connect");
            socket = new Socket(host,port);
            System.out.println("socket");
            out = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("outt");
            new Thread(this).start();
            
            System.out.println("in");
            //listener = new EventListener();
            
            System.out.println("connected");
            //createAndShowGUI(); // Create the GUI
            System.out.println("Connecting");
        }catch(ConnectException e){
            System.out.println("Unable to connect to the server.");
        }catch(IOException e){
            System.out.println("IOException: Unable to connect to the server.");
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
            System.out.println("closed");
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    //send data to server
    public void sendObject(Object packet){
        try{
            System.out.println("out");
            if(out == null){
                out = new ObjectOutputStream(socket.getOutputStream());
                new Thread(this).start();
            }
            out.writeObject(packet);
            System.out.println("send");
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
                   in = new ObjectInputStream(socket.getInputStream());
                   new Thread(this).start();
                   Object data = in.readObject();
                   if (data instanceof SignalPacket){
                        
                       SignalPacket signalPacket = (SignalPacket)data;
                        final String message = signalPacket.getMessage();
                         //System.out.println("Signal received from server: " + message);
                         if(message.equals("Start Game") ){
                             window.startTetris(); 
                         }
                        
                    } else {
                        System.out.println("Server no response");
                    }
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
*/