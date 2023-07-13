/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packets;

import java.util.List;
import java.io.Serializable;

/**
 *
 * @author User
 */
public class StartGamePacket implements Serializable{
    public int onlineUser;
    private List<Integer> userIdList;
    
    public StartGamePacket(int onlineUser, List<Integer> userIdList){
        this.onlineUser = onlineUser;
        this.userIdList = userIdList;
    }
    
    public int getUserCount(){
        return onlineUser;
    }
    
    public List<Integer> getUserIdList(){
        return userIdList;
    }
}
