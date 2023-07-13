/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tetris;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import packets.Attack;
import packets.BoardPacket;
import packets.ShapeListPacket;
import packets.StartGamePacket;

/**
 *
 * @author User
 */



public class ObjectHandler {
    private WindowGame window;
    private Board board;
    private int count;
    private Client client;
    private BoardHandler boardHandler;
    
    public ObjectHandler(WindowGame window, Board board, Client client){
        this.window = window;
        this.board = board;
        this.count = 0;
        this.client = client;
    }
    
    public void handleMessage(Object obj){
        if (obj instanceof String) {
            String message = (String) obj;
            System.out.println("Received from server: " + message);
            
            /*if(message.equals("Start Game")){
                window.startTetris();
            }else*/ 
            if(message.equals("Win Game")){
                board.setWinGame();
            }
        }else if (obj instanceof Integer){
            int clientId = (Integer) obj;
            boardHandler.addClient(clientId);
            
        }else if(obj instanceof StartGamePacket)
        {
            System.out.println("Start Game");
            client.writeMessages("Ready");
            StartGamePacket startGamePacket = (StartGamePacket) obj;
            boardHandler = new BoardHandler(startGamePacket.getUserCount(), startGamePacket.getUserIdList());
            
            board.setBoardHandler(boardHandler);
            //TimeUnit.SECONDS.sleep(1);
            window.startTetris();
            
        }else if(obj instanceof ShapeListPacket){
            System.out.println("Received shape list");
            if(((ShapeListPacket) obj).getId() >= count){
                System.out.println("Adding shape list");
                board.addShapeList(((ShapeListPacket) obj).getShapeList());
                count++;
            }else{
                System.out.println("Not adding shape list");
            }
        }else if(obj instanceof Attack){
            Attack row = (Attack) obj;
            board.addNewRowQueue(row.getRow());
            System.out.println("being attack " + row.getRow() + " rows");
        }else if(obj instanceof BoardPacket){
            BoardPacket boardPacket = (BoardPacket) obj;
            boardHandler.setBoard(boardPacket.getUserId(), boardPacket.getBoard());
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
