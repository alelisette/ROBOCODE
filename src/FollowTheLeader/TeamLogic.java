/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FollowTheLeader;

import robocode.ScannedRobotEvent;

/**
 *
 * @author marc
 */
public class TeamLogic {
    private final RealStealTeam _r;
    private ScannedRobotEvent _enemy;
    private boolean _scanIzquierda;
    private boolean _scanDerecha;
    private boolean _scanCentro;
    private int _contador = 0;
   
    public TeamLogic(RealStealTeam r){
        _r = r;
        _scanIzquierda = true;
        _scanDerecha = false;
        _scanCentro = false;
        _contador = 0;
    }
    
    public void moverACoordenadas(double destinoX, double destinoY) {
        // Posición actual del robot
        double xActual = _r.getX();
        double yActual = _r.getY();

        // Calcular la distancia al punto de destino
        double distancia = Math.hypot(destinoX - xActual, destinoY - yActual);

        // Calcular el ángulo absoluto hacia el punto de destino
        double anguloDestino = Math.toDegrees(Math.atan2(destinoX - xActual, destinoY - yActual));

        // Convertir el ángulo del destino a un ángulo relativo respecto a la orientación actual del robot
        double anguloRelativo = normalizarAngulo(anguloDestino - _r.getHeading());

        // Lógica de esquiva de enemigos u obstáculos
        if (_enemy != null && _enemy.getDistance() < 250.0 && _contador!=0) {  // Si hay un enemigo cerca
            
            if (_enemy.getDistance() < 120) {
                _r.setBack(50);  // Retroceder si el enemigo está demasiado cerca
            } 

            // Girar para esquivar al enemigo, ajustando según el ángulo de detección
            double esquivaAngulo = -_enemy.getBearing();
            _r.setTurnRight(esquivaAngulo);
            _contador -= 1;
            _r.setAhead(10);
            
        } else {  // Si no hay enemigos cerca o fuera del rango de esquiva
            // Girar hacia el punto de destino
            _r.setTurnRight(anguloRelativo);
        }
        
        System.out.println("anguloRelativo = " + anguloRelativo);    

        // Solo moverse hacia adelante si el giro se ha completado
        if (compareDoubles(anguloRelativo, 0.0)) {
            _r.setAhead(distancia);
        } 
    }
        
    public void dispara(){
        /*if (Math.abs(_r.getTurnRemaining()) < 10 && _contador != 0) {
            _r.setAdjustRadarForGunTurn(true);

            // Calcular la potencia de disparo inversamente proporcional a la distancia
            double firePower = Math.min(700 / _enemy.getDistance(), 3); // Máximo de potencia es 3
            firePower = Math.max(firePower, 0.1); // Potencia mínima razonable (ej. 0.1)

            // Calcular el ángulo hacia el enemigo
            double angleToEnemy = _r.getHeading() + _enemy.getBearing();  // Angulo hacia el enemigo
            double gunTurnAngle = normalizarAngulo(angleToEnemy - _r.getGunHeading());  // Ángulo que debe girar el cañón

            // Girar el cañón hacia el enemigo
            _r.setTurnGunRight(gunTurnAngle);

            // Verificar si el cañón está alineado con el enemigo para disparar
            if (Math.abs(gunTurnAngle) < 5) {  // Solo disparar si el cañón está casi alineado
                System.out.println("SetFire " + firePower + "\n");
                _r.setFire(firePower);
            }
        }*/
    }

    public double normalizarAngulo(double angulo) {
        while (angulo > 180) {
            angulo -= 360;
        }
        while (angulo < -180) {
            angulo += 360;
        }
        return angulo;
    }
    
    public void escanejar(){
        // Escaneamos obstaculos:
        if (_scanIzquierda) {
            // Gira 22.5 grados hacia la izquierda desde la posición actual
            _r.setTurnRadarLeft(15);
            _scanIzquierda = false;  // Ja hem escanejat l'esquerra
            _scanCentro = true;      // Ara tornem al centre
        } else if (_scanCentro) {
            // Gira 45 graus a la dreta per a cobrir el centre i la dreta
            _r.setTurnRadarRight(30);
            _scanCentro = false;    // Ja hem escanejat el centre
            _scanDerecha = true;    // Ara anem a la dreta
        } else if (_scanDerecha) {
            // Gira 22.5 graus a l'esquerra per a tornar al centre
            _r.setTurnRadarLeft(15);
            _scanDerecha = false;  
            _scanIzquierda = true;  // Tornem a l'esquerra per a reiniciar el cicle
        }
    }
    
    //Comprar dos doubles sense errors de imprecisions.
    public boolean compareDoubles(double num1, double num2, double epsilon){
        //Comprar dos doubles sin errores de inprecision. 
        boolean result = Math.abs(num1-num2) < epsilon;
        return result;
    }
    
    public boolean compareDoubles(double num1, double num2){
        return compareDoubles(num1, num2, 0.1);
    }
    
    public void setEnemy(ScannedRobotEvent e){
        _enemy = e;
        _contador = 2;
        boolean isTeammate = false;
        for (String teammate : _r.getTeammates()) {
            if (teammate.equals(e.getName())) {
                isTeammate = true;
            }
        }
        if(!isTeammate){
            dispara();
        }
    }
}
