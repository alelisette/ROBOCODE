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
    private double _moviment;
    
    public void DemoRobocde(){
        _mou = false;
        _gira = false;
        _graus = 0.0;
        _moviment = 0.0;
    }
    
    @Override
    public void run() {
        _e = new EstatTimidin_0(this);
        final double ampleTerreny = this.getBattleFieldWidth();
        final double alturaTerreny = this.getBattleFieldHeight();
        final double margenParedes = 27.5d;
        while(true) {
            //System.out.println("Grados Radar: " + this.getRadarHeading() + "\n");    
            if(_gira==true){
                //Comprar dos doubles sin errores de inprecision. -------------------------------
                double epsilon = 0.5d;
                boolean result = Math.abs(this.getHeading()-_graus) < epsilon;
                //Comprobem si hem girat els suficients graus
                //System.out.println("Resultado: " + Math.abs(this.getHeading()-_graus) + "Resta: " + this.getHeading() + " - " + _graus);
                if(result==false){
                    this.setTurnRight(1);
                } else {
                    _mou = true;
                } 
                // ------------------------------------------------------------------------------
                //Comprar dos doubles sin errores de inprecision. -------------------------------
                epsilon = 0.000001d;
                result = Math.abs(this.getRadarHeading() - this.getHeading()) < epsilon;
                //Comprobem si hem girat els suficients graus
                if(result==false){
                    this.setTurnRadarRight(10);
                } else if(_mou){
                    _gira = false;
                }
                // ------------------------------------------------------------------------------           */     
            }
            if(_mou==true){
                //System.out.println("moviendo!");
                //System.out.println("x: " + this.getX() + "y: " + this.getY());
                if((ampleTerreny <= this.getX()+margenParedes) || (this.getX()-margenParedes <= 0.0d) || 
                        (alturaTerreny <= this.getY()+margenParedes) || (this.getY()-margenParedes <= 0.0d)){
                    System.out.println("Stop!");
                    _mou = false;
                } else {
                    this.setAhead(15);
                }
            }
            if((_mou==false) && (_gira==false) && (_graus==0.0)){
                
                this.setTurnRadarRight(10);
            } else {
            
            }
           // this.ahead(100); //s'hand e fer cride asincrones no ssincrones
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
    
    public void setEstat(Estat nouEstat){
        _e = nouEstat;
    }
    
    public boolean getGira(){
        return _gira;
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
    
    public void setMoviment(double mov){
        _moviment = mov;
    }
    
    // Mètode per calcular la distància entre dos punts
    public double distancia(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
            
}
    
    