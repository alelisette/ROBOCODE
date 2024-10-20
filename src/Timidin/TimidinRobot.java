/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Timidin;

/**
 *
 * @author marc
 */
import java.awt.Color;
import robocode.*;

public class TimidinRobot extends AdvancedRobot{
    //Funcions i logica pel funcionament del Timidin
    LogicaRobot logic;
    
    private void initTimidinRobot(){
        this.setBodyColor(Color.BLACK);
        this.setGunColor(Color.RED);
        this.setRadarColor(Color.RED);
        this.setScanColor(Color.RED);
        this.setBulletColor(Color.GREEN);
        logic = new LogicaRobot();
    }
    
    @Override
    public void run() {
        initTimidinRobot();
        logic.setEstat(new EstatTimidin_0(this));   
        while(true) {
            logic.getEstat().torn();
            execute();
        }
    }   
    
    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        logic.getEstat().onScannedRobot(event);
    }
    
    @Override
    public void onHitRobot(HitRobotEvent event){
        logic.getEstat().onHitRobot(event);
    }
    
    @Override
    public void onHitWall(HitWallEvent event){
        logic.getEstat().onHitWall(event);
    }
    
    @Override
    public void onRobotDeath(RobotDeathEvent event){
        logic.getEstat().onRobotDeath(event);
    }
}
    
    