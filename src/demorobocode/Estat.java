/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package demorobocode;

import robocode.Robot;
import robocode.ScannedRobotEvent;

/**
 *
 * @author marc
 */
public abstract class Estat {
    protected TimidinRobot _r;
    
    public Estat(TimidinRobot r){
        _r = r;
    }
    
    //abstract void torn();
    abstract void onScannedRobot(ScannedRobotEvent e);
}
