/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packets;

import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author Cheh Shu Ze
 */
public class ShapeListPacket implements Serializable{
    private LinkedList<Integer> shapeList;
    private int id;
    
    public ShapeListPacket(int id, LinkedList<Integer> shapeList){
        this.shapeList = shapeList;
        this.id = id;
    }
    
    public LinkedList<Integer> getShapeList(){
        return shapeList;
    }
    
    public int getId(){
        return id;
    }
}