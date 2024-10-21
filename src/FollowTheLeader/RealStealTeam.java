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
public class RealStealTeam extends TeamRobot {
    private Estat _estat;
    protected TeamLogic _logic;
    
    @Override
    public void run(){
        _estat = new HandShake(this);
        _logic = new TeamLogic(this);
        while(true){
            _estat.torn();
            execute();
        }
    }
    
    @Override
    public void onMessageReceived(MessageEvent e){
        _estat.onMessageReceived(e);
    }
    
    @Override
    public void onPaint(Graphics2D g) {
        _estat.onPaint(g);
    }
    
    @Override
    public void onScannedRobot(ScannedRobotEvent event){
        _estat.onScannedRobot(event);
    }
    
    @Override
    public void onRobotDeath(RobotDeathEvent event){
        _estat.onRobotDeath(event);
    }
    
    @Override
    public void onHitRobot(HitRobotEvent event){
        _estat.onHitRobot(event);
    }
    
    public void setEstat(Estat nouEstat){
        _estat = nouEstat;
    }
}
