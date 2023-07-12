/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.packets;

import java.io.Serializable;

public class SignalPacket implements Serializable {
    private String message;
    private String hostName;

    public SignalPacket(String message, String hostName) {
        this.message = message;
        this.hostName = hostName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getHostName(){
        return hostName;
    }
    
    public void setHostName(String hostName){
        this.hostName = hostName;
    }
    
}
