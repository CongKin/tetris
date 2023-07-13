/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packets;

import com.mycompany.tetris.Shape;
import java.awt.Color;
import java.io.Serializable;

/**
 *
 * @author User
 */
public class BoardPacket implements Serializable{
    private Color[][] board;
    private int[][] shape;
    private Color shapeColor;
    private int x,y;
    private String clientName;
    private int clientId; 
    
    public BoardPacket(Color[][] board, int[][] shape, Color shapeColor, int x, int y){
        this.board = board;
        this.shape = shape;
        this.shapeColor = shapeColor;
        this.x = x;
        this.y = y;
    }
    
    public void setIdentity(int clientId, String clientName){
        this.clientId = clientId;
        this.clientName = clientName;
    }
    
    public void combine(){
        for(int row=0; row<shape.length; row++){
            for(int col=0; col<shape[0].length; col++){
                if(shape[row][col]!=0){
                    board[x+row][y+col] = shapeColor;
                }
            }
        }
    }
    
    public Color[][] getBoard(){
        return board;
    }
    
    public int getUserId(){
        return clientId;
    }
}
