/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FollowTheLeader;

import java.awt.Color;
import java.awt.Graphics2D;
import robocode.MessageEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

/**
 *
 * @author marc
 */
public class EstatSeguidor extends Estat {

    public EstatSeguidor(RealStealTeam r) {
        super(r);
        // Cambiamos el color:
        _r.setBodyColor(Color.ORANGE);
        _r.setGunColor(Color.ORANGE);
        _r.setRadarColor(Color.ORANGE);
        _r.setScanColor(Color.ORANGE);
        _r.setBulletColor(Color.ORANGE);
    }

    @Override
    void torn() {
        System.out.println("Seguidor Conga Activa");    
    }

    @Override
    void onScannedRobot(ScannedRobotEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    void onMessageReceived(MessageEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    void onRobotDeath(RobotDeathEvent event) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    void onPaint(Graphics2D g) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
