/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FollowTheLeader;

import java.io.Serializable;

/**
 *
 * @author marc
 */
public class Message implements Serializable{
    private String tlName;
    private String sender;

    public Message(String tlName) {
        this.tlName = tlName;
    }

    public String getTlName() {
        return tlName;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
