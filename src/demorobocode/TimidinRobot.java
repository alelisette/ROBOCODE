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

public class TimidinRobot extends AdvancedRobot{
    private Estat _e;
    private boolean _gira;
    private double _graus;
    private boolean _mou;
    private double _movimentX;
    private double _movimentY;
    private double _moviment;
    
    public void DemoRobocde(){
        _mou = false;
        _movimentX = 0.0;
        _movimentY = 0.0;
        _gira = false;
        _graus = 0.0;
        _moviment = 0.0;
    }
    
    @Override
    public void run() {
        _e = new EstatTimidin_0(this);
        while(true) {
            //System.out.println("Grados Radar: " + this.getRadarHeading() + "\n");    
            if(_gira==true){
             //   this.setTurnGunRight(_graus);
            }
            if(_mou==true){
                this.setAhead(_moviment);
            }
            if((_mou==false) && (_gira==false)){
                this.setTurnGunRight(1);
            }
           // this.ahead(100); //s'hand e fer crides asincrones no ssincrones
           // this.turnRight(90);
            //EN els events no fer res, guardar flags o que es mou, NO es dispara no es gira res i no premer accions 
            //onScined on hit guardo el estat en le run si 
            
            //this.setAhead(100);        
            //this.setTurnRight(90);
            execute();
        }
        
       // team.members = 
    }
    
    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        _e.onScannedRobot(event);
    }
    
    private void setEstat(Estat nouEstat){
        _e = nouEstat;
    }
    
    public void setGira(boolean nouEstat){
        _gira = nouEstat;
    }
    
    public void setGraus(double graus){
        _graus = graus;
    }
    public void setMou(boolean nouEstat){
        _mou = nouEstat;
    }
    
    public void setMoviment(double x, double y){
        _movimentX = x;
        _movimentY = y;
    }
    
            
}
    
    