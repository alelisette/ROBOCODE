/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Timidin;

import robocode.*;
import java.awt.geom.Point2D;

/**
 * La classe EstatTimidin_1 representa un estat intermedi en la lògica del robot Timidin.
 * En aquest estat, el robot es mou cap a la cantonada més allunyada i escaneja l'entorn 
 * mentre es prepara per a una possible confrontació amb altres robots.
 * 
 * @author marc
 */
public class EstatTimidin_1 extends Estat {
    
    private boolean _scanIzquierda;
    private boolean _scanDerecha;
    private boolean _scanCentro;
    private int _contador;

    /**
     * Constructor de la classe EstatTimidin_1.
     * Inicialitza l'estat associat al robot i defineix les variables de control 
     * per l'escaneig de l'entorn.
     * 
     * @param robot El robot Timidin associat amb aquest estat.
     */
    public EstatTimidin_1(TimidinRobot robot) {
        super(robot);
        System.out.println("ESTAT 1\n");
        _scanIzquierda = true;
        _scanDerecha = false;
        _scanCentro = false;
        _contador = 0;
    }

    /**
     * Aquest mètode s'executa en cada torn. El robot es mou cap a la cantonada
     * més allunyada i escaneja l'entorn per detectar altres robots.
     * Si arriba a la cantonada, canvia a l'estat EstatTimidin_2.
     */
    @Override
    void torn() {
        mou(_r.logic.getCantonadaMesLluny());
        escanejar();
        if (_r.logic.compareDoubles(_r.getX(), _r.logic.getCantonadaMesLluny().x, 40) && 
            _r.logic.compareDoubles(_r.getY(), _r.logic.getCantonadaMesLluny().y, 40)) {
            System.out.println("FINAL ESTAT 1\n");
            _r.logic.setEstat(new EstatTimidin_2(_r));
        }
    }

    /**
     * Gestió de l'esdeveniment onScannedRobot. Quan el robot detecta un altre
     * robot, decideix el moviment a realitzar en funció de la distància a l'enemic.
     * 
     * @param e L'esdeveniment de detecció d'un altre robot.
     */
    @Override 
    public void onScannedRobot(ScannedRobotEvent e) {
        System.out.println("==================================\n");
        System.out.println("Scanned\n");
        System.out.println("Distancia: " + e.getDistance());
        if (e.getDistance() < 250.0) {
            if (e.getDistance() < 100) {
                _contador = 1;
                _r.setBack(50);
            }
            _r.setTurnRight(-(300 / e.getBearing()));
        }
    }

    /**
     * Mètode privat que controla el moviment del robot cap a un punt determinat
     * del camp de batalla. Es recalcula l'angle i la distància per arribar-hi.
     * 
     * @param punt El punt de destí cap al qual es mou el robot.
     */
    private void mou(Point2D.Double punt) {
        _r.logic.recalcularAngleCantonada(_r.getX(), _r.getY(), _r.getHeading());
        _r.turnRight(_r.logic.getGraus() / 2);
        if (_contador == 0) {
            _r.setAhead(Math.hypot(punt.x - _r.getX(), punt.y - _r.getY()) / 5);
        } else {
            _contador -= 1;
        }
    }

    /**
     * Mètode privat que controla l'escaneig del radar en tres direccions: esquerra, 
     * centre i dreta. Cada torn escaneja en una direcció diferent per cobrir
     * el màxim d'espai possible.
     */
    private void escanejar() {
        if (_scanIzquierda) {
            _r.setTurnRadarLeft(22.5);
            _scanIzquierda = false;
            _scanCentro = true;
        } else if (_scanCentro) {
            _r.setTurnRadarRight(45);
            _scanCentro = false;
            _scanDerecha = true;
        } else if (_scanDerecha) {
            _r.setTurnRadarLeft(22.5);
            _scanDerecha = false;
            _scanIzquierda = true;
        }
    }

    /**
     * Gestió de l'esdeveniment onHitRobot. Quan el robot col·lideix amb un altre
     * robot, realitza un petit retrocés i gira 90 graus per evitar més col·lisions.
     * 
     * @param e L'esdeveniment de col·lisió amb un altre robot.
     */
    @Override
    void onHitRobot(HitRobotEvent e) {
        _r.setBack(40);
        _r.turnRight(90);
    }

    /**
     * Gestió de l'esdeveniment onHitWall. Quan el robot col·lideix amb una paret,
     * ajusta la seva direcció per desviar-se de la paret i avançar.
     * 
     * @param e L'esdeveniment de col·lisió amb una paret.
     */
    @Override
    void onHitWall(HitWallEvent e) {
        System.out.println("Wall Hit\n");
        _r.setTurnRight(-e.getBearing());
        _r.setAhead(10);
    }

    /**
     * Gestió de l'esdeveniment onRobotDeath. Actualment no implementat perque no ens fa falta en aquest estat del Timidin.
     * 
     * @param e L'esdeveniment de la mort d'un altre robot.
     * @throws UnsupportedOperationException Sempre es llança, ja que aquest mètode
     *         encara no està implementat.
     */
    @Override
    void onRobotDeath(RobotDeathEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
