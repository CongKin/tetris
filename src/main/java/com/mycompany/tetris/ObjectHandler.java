/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tetris;

import java.util.LinkedList;
import packets.ShapeListPacket;

/**
 *
 * @author User
 */



public class ObjectHandler {
    private WindowGame window;
    private Board board;
    private int count;
    
    public ObjectHandler(WindowGame window, Board board){
        this.window = window;
        this.board = board;
        this.count = 0;
    }
    
    public void handleMessage(Object obj){
        if (obj instanceof String) {
            String message = (String) obj;
            System.out.println("Received from server: " + message);
            
            if(message.equals("Start Game")){
                window.startTetris();
            }
        }else if(obj instanceof ShapeListPacket){
            System.out.println("Received shape list");
            if(((ShapeListPacket) obj).getId() >= count){
                System.out.println("Adding shape list");
                board.addShapeList(((ShapeListPacket) obj).getShapeList());
                count++;
            }else{
                System.out.println("Not adding shape list");
            }
        }
        /*
        if(obj instanceof LinkedList){
            System.out.println("Received linked list");
            LinkedList<?> linkedList = (LinkedList<?>) obj;
            if(!linkedList.isEmpty() && linkedList.getFirst() instanceof Integer){
                System.out.println("add shape list");
                LinkedList<Integer> shapeList = (LinkedList<Integer>) obj;
                board.addShapeList(shapeList);
            }
        }*/
    }
}
