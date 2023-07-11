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

/**
 *
 * @author User
 */
public class Shape {
    private int x = 4, y = 0;
    private int normal = 600;
    private int fast = 50;
    private int delayTimeForMovement = normal;
    private long beginTime;
    
    private int shadowThickness = 3;
    
    private int deltaX = 0;
    private boolean collision = false;
    
    private int[][] coords;
    private Board board;
    private Color color;
    
    private int number;
    
    
    public Shape(int[][] coords, Board board, Color color, int number){
        this.coords = coords;
        this.board = board;
        this.color = color;
        this.number = number;
    }
    
    public void setX(int x){
        this.x = x;
    }
    
    public void setY(int y){
        this.y = y;
    }
    
    public void reset(){
        this.x=4;
        this.y=0;
        collision = false;
        speedDown();
    }
    
    public void update(){
        if(collision){
            // fill the color for board
            for(int row = 0; row <coords.length; row++){
                for(int col=0; col<coords[0].length; col++){
                    if(coords[row][col] != 0){
                        board.getBoard()[y+row][x+col] = color;
                    }
                }
            }
            
            checkLine();
            // set current shape
            board.setCurrentShape();
            return;
        }

        // check moving horizontal
        boolean moveX=true;
        if(!(x+deltaX + coords[0].length > BOARD_WIDTH) && !(x+deltaX <0)){
            for(int row=0; row<coords.length; row++){
                for(int col=0; col<coords[row].length;col++){
                    if(coords[row][col]!=0){
                        if(board.getBoard()[y+row][x+deltaX+col]!=null){
                            moveX = false;
                        }
                    }
                }
            }
            if(moveX){
                x+=deltaX;
            }
        }
        deltaX = 0;

        if(System.currentTimeMillis() - beginTime > delayTimeForMovement){
            // vertical movement
            if(!(y + 1 + coords.length>BOARD_HEIGHT)){
                for(int row=0; row<coords.length; row++){
                    for(int col=0; col<coords[row].length; col++){
                        if(coords[row][col] != 0){
                            if(board.getBoard()[y+1+row][x+deltaX+col] != null){
                                collision = true;
                            }
                        }
                    }
                }
                if(!collision){
                    y++;
                }
            } else{
                collision = true;
            }

            beginTime=System.currentTimeMillis();
        }
    }
    
    private void checkLine(){
        int bottomLine = board.getBoard().length - 1;
        int rowCleared = 0;
        for (int topLine = board.getBoard().length-1; topLine>0; topLine--){
            int count =0;
            for (int col=0; col < board.getBoard()[0].length; col++){
                if(board.getBoard()[topLine][col] != null){
                    count++;
                }
                board.getBoard()[bottomLine][col] = board.getBoard()[topLine][col];
            }
            if (count < board.getBoard()[0].length){
                bottomLine--;
            }else{
                rowCleared++;
            }
        }
        if(rowCleared>0){
            board.clearRow(rowCleared);
        }
    }
    
    public int getDropY(){
        int tempY=y;
        boolean collisionTemp = false;
        while(!collisionTemp){
            if(!(tempY + coords.length>BOARD_HEIGHT)){
                for(int row=0; row<coords.length; row++){
                    for(int col=0; col<coords[row].length; col++){
                        if(coords[row][col] != 0){
                            if(board.getBoard()[tempY+row][x+deltaX+col] != null){
                                collisionTemp = true;
                            }
                        }
                    }
                }
                if(!collisionTemp){
                    tempY++;
                }
            } else{
                collisionTemp = true;
            }
        }
        return tempY;
    }
    
    public void placeShape(){
        int tempY=getDropY();
        collision = true;
        y = tempY - 1;
    }
    
    public void rotateShape(){
        if(!collision){
            int[][] rotatedShape = transposeMatrix(coords);
            //reverseRows(rotatedShape);
            // check for right side and bottom
            if((x+rotatedShape[0].length > Board.BOARD_WIDTH)||(y+rotatedShape.length > Board.BOARD_HEIGHT)){
                return;
            }

            // check for collision with other shapes before related
            for(int row=0;row<rotatedShape.length;row++){
                for(int col=0;col<rotatedShape[row].length;col++){
                    if(rotatedShape[row][col] != 0){
                        if(board.getBoard()[y + row][x+col]!=null){
                            return;
                        }
                    }
                }
            }
            coords = rotatedShape;
        }
    }
    
    private int[][] transposeMatrix(int[][] matrix){
        int[][] temp = new int[matrix[0].length][matrix.length];
        for (int row=0; row<matrix.length; row++){
            for(int col=0; col<matrix[0].length; col++){
                temp[col][matrix.length-row-1] = matrix[row][col];
            }
        }
        return temp;
    }
    
    private void reverseRows(int[][] matrix){
        
        int middle = matrix.length / 2;
        for(int row=0; row<middle;row++){
            int[] temp = matrix[row];
            matrix[row] = matrix[matrix.length-row-1];
            matrix[matrix.length-row-1] = temp;
        }
    }
    
    public void render(Graphics g){
        // draw the shape
        for(int row=0; row<coords.length; row++){
            for(int col=0; col<coords[0].length; col++){
                if(coords[row][col]!=0){
                    g.setColor(color);
                    g.fillRect(col * BLOCK_SIZE + x*BLOCK_SIZE, row*BLOCK_SIZE + y*BLOCK_SIZE,BLOCK_SIZE,BLOCK_SIZE);
                }
            }
        }
        
        // draw the shadow
        int tempY = getDropY()-1;
        for(int row=0; row<coords.length; row++){
            for(int col=0; col<coords[0].length; col++){
                if(coords[row][col]!=0){
                    g.setColor(Color.lightGray);
                    g.fillRect((x+col)*BLOCK_SIZE - shadowThickness/2, (row+tempY)*BLOCK_SIZE - shadowThickness/2, BLOCK_SIZE+shadowThickness, shadowThickness);
                    g.fillRect((x+col)*BLOCK_SIZE - shadowThickness/2, (row+tempY)*BLOCK_SIZE - shadowThickness/2, shadowThickness, BLOCK_SIZE+shadowThickness);
                    g.fillRect((x+col)*BLOCK_SIZE - shadowThickness/2, (row+tempY+1)*BLOCK_SIZE - shadowThickness/2, BLOCK_SIZE+shadowThickness, shadowThickness);
                    g.fillRect((x+col+1)*BLOCK_SIZE - shadowThickness/2, (row+tempY)*BLOCK_SIZE - shadowThickness/2, shadowThickness, BLOCK_SIZE+shadowThickness);
                    
                }
            }
        }
    }
    
    public int[][] getCoords(){
        return coords;
    }
    
    public Color getColor(){
        return color;
    }
    
    public int getNumber(){
        return number;
    }
    
    public void speedUp(){
        delayTimeForMovement = fast;
    }
    
    public void speedDown(){
        delayTimeForMovement = normal;
    }
    
    public void moveRight(){
        deltaX=1;
    }
    
    public void moveLeft(){
        deltaX=-1;
    }
    
    public int getY(){
        return y;
    }
    
    public int getX(){
        return x;
    }
    
    
}


