/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Timidin;

import robocode.*;
import java.awt.geom.Point2D;

/**
 *
 * @author marc
 */
public class EstatTimidin_1 extends Estat {
    
    private boolean _scanIzquierda;
    private boolean _scanDerecha;
    private boolean _scanCentro;
    private int _contador;
    
    public EstatTimidin_1(TimidinRobot robot) {
        super(robot);
        System.out.println("ESTAT 1\n");
        _scanIzquierda = true;
        _scanDerecha = false;
        _scanCentro = false;
        _contador = 0;
    }
    
    @Override
    void torn(){
        mou(_r.logic.getCantonadaMesLluny());
        escanejar();
        if(_r.logic.compareDoubles(_r.getX(), _r.logic.getCantonadaMesLluny().x, 40) && 
                _r.logic.compareDoubles(_r.getY(), _r.logic.getCantonadaMesLluny().y, 40)){
            System.out.println("FINAL ESTAT 1\n");
            _r.logic.setEstat(new EstatTimidin_2(_r));
        } 
    }
    
    @Override 
    public void onScannedRobot(ScannedRobotEvent e){
        System.out.println("==================================\n");
        System.out.println("Scanned\n");
        System.out.println("Distancia: "+ e.getDistance());
        if(e.getDistance() < 250.0) {
            if(e.getDistance() < 100){
                _contador = 1;
                _r.setBack(50);
            } 
            _r.setTurnRight(-(300/e.getBearing()));
        }
    }
    
    
    private void mou(Point2D.Double punt) {
        _r.logic.recalcularAngleCantonada(_r.getX(), _r.getY(), _r.getHeading());
        _r.turnRight(_r.logic.getGraus()/2);
        if(_contador==0){
            _r.setAhead(Math.hypot(punt.x-_r.getX(), punt.y-_r.getY())/5);
        } else {
            _contador -= 1;
        }
    }
    
    private void escanejar(){
        // Escaneamos obstaculos:
        if (_scanIzquierda) {
            // Gira 22.5 grados hacia la izquierda desde la posición actual
            _r.setTurnRadarLeft(22.5);
            _scanIzquierda = false;  // Ja hem escanejat l'esquerra
            _scanCentro = true;      // Ara tornem al centre
        } else if (_scanCentro) {
            // Gira 45 graus a la dreta per a cobrir el centre i la dreta
            _r.setTurnRadarRight(45);
            _scanCentro = false;    // Ja hem escanejat el centre
            _scanDerecha = true;    // Ara anem a la dreta
        } else if (_scanDerecha) {
            // Gira 22.5 graus a l'esquerra per a tornar al centre
            _r.setTurnRadarLeft(22.5);
            _scanDerecha = false;  
            _scanIzquierda = true;  // Tornem a l'esquerra per a reiniciar el cicle
        }
    }
    
    @Override
    void onHitRobot(HitRobotEvent e) {        
        _r.setBack(40);
        _r.turnRight(90);
    }

    @Override
    void onHitWall(HitWallEvent e) {
        System.out.println("Wall Hit\n");
        _r.setTurnRight(-e.getBearing());
        _r.setAhead(10);    
    }

    @Override
    void onRobotDeath(RobotDeathEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
