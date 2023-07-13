/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tetris;

import static com.mycompany.tetris.Board.BLOCK_SIZE;
import static com.mycompany.tetris.Board.BOARD_HEIGHT;
import static com.mycompany.tetris.Board.BOARD_WIDTH;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class BoardHandler {
    private int onlineCount;
    private List<Integer> userIdList;
    private List<Integer> gamerList;
    private Color[][][] boards;
    private int rowCount;
    
    public BoardHandler(int count, List<Integer> userIdList){
        this.onlineCount = count;
        this.userIdList = userIdList;
        gamerList = new ArrayList<>();
        rowCount = 1;
    }
    
    public void addClient(int id){
        gamerList.add(id);
    }
    
    public void setBoard(int id, Color[][] board){
        boards[gamerList.indexOf(id)] = board;
    }
    
    public void render(Graphics g){
        if(gamerList.isEmpty()){
            for(int row=0; row<boards[0].length; row++){
                for(int col=0; col<boards[0][row].length; col++){
                    if(boards[0][row][col]!=null){
                        g.setColor(boards[0][row][col]);
                        g.fillRect(col * BLOCK_SIZE + BLOCK_SIZE*17, row*BLOCK_SIZE ,BLOCK_SIZE,BLOCK_SIZE);
                    }
                }
            }
            
            g.setColor(Color.lightGray);
            for(int row=0; row<BOARD_HEIGHT+1; row++)
                g.drawLine(0 + BLOCK_SIZE*17, BLOCK_SIZE*row, BLOCK_SIZE*BOARD_WIDTH + BLOCK_SIZE*17, BLOCK_SIZE*row);
        }
    }
}
