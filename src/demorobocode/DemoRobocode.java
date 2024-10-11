/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package demorobocode;

/**
 *
 * @author Aleli
 */
import robocode.AdvancedRobot;
import robocode.Robot;
import robocode.ScannedRobotEvent;

public class DemoRobocode extends AdvancedRobot{
    @Override
    public void run() {
    
        while(true) {
           // this.ahead(100); //s'hand e fer crides asincrones no ssincrones
           // this.turnRight(90);
            //EN els events no fer res, guardar flags o que es mou, NO es dispara no es gira res i no premer accions 
            //onScined on hit guardo el estat en le run si 
            
            this.setAhead(100);
            
            this.setTurnRight(90);
            execute();
        }
        
       // team.members = 
    }
    
    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
    
    fire(1); 
    
   // fire(Rules.)
    }
            
}
    
    