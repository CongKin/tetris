/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packets;

import java.io.Serializable;

/**
 *
 * @author User
 */
public class Attack implements Serializable{
    private int row;
    
    public Attack(int row){
        this.row = row;
    }
    
    public int getRow(){
        return row;
    }
}
