/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FollowTheLeader;

import java.awt.Graphics2D;
import robocode.*;

/**
 *
 * @author marc
 */
public abstract class Estat  {
    RealStealTeam _r;
        
    public Estat(RealStealTeam r){
        _r = r;
    }
    
    abstract void torn();
    abstract void onScannedRobot(ScannedRobotEvent e);
    abstract void onMessageReceived(MessageEvent e);
    abstract void onRobotDeath(RobotDeathEvent event);
    abstract void onPaint(Graphics2D g);
}
