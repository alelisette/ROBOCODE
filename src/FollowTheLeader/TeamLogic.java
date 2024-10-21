/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FollowTheLeader;

import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import robocode.ScannedRobotEvent;

/**
 *
 * @author marc
 */
public class TeamLogic {
    private final RealStealTeam _r;
    private ScannedRobotEvent _e;
    private static String _enemy;
    private static double _enemyX;
    private static double _enemyY;
    private static Map<String, Integer> jerarquia = new HashMap<>(); // Jerarquía global
    private boolean _scanIzquierda;
    private boolean _scanDerecha;
    private boolean _scanCentro;
    private int _contador = 0;
   
    public TeamLogic(RealStealTeam r){
        _r = r;
        _enemy = null;
        _enemyX = 0.0;
        _enemyY = 0.0;
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
        if (_e != null && _e.getDistance() < 250.0 && _contador!=0) {  // Si hay un enemigo cerca
            
            if (_e.getDistance() < 120) {
                _r.setBack(50);  // Retroceder si el enemigo está demasiado cerca
            } 

            // Girar para esquivar al enemigo, ajustando según el ángulo de detección
            double esquivaAngulo = -_e.getBearing();
            _r.setTurnRight(esquivaAngulo);
            _contador -= 1;
            _r.setAhead(30);
            
        } else {  // Si no hay enemigos cerca o fuera del rango de esquiva
            // Girar hacia el punto de destino
            _r.setTurnRight(anguloRelativo);
        }
        
        //System.out.println("anguloRelativo = " + anguloRelativo);    

        // Solo moverse hacia adelante si el giro se ha completado
        if (compareDoubles(anguloRelativo, 0.0)) {
            _r.setAhead(distancia);
        } 
    }
    
    public void moverSeguidorACoordenadas(double destinoX, double destinoY) {
                
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
        if (_e != null && _e.getDistance() < 250.0 && _contador!=0 && !isTeammate()) {  // Si hay un enemigo cerca
            
            if (_e.getDistance() < 120) {
                _r.setBack(100);  // Retroceder si el enemigo está demasiado cerca
            } 

            // Girar para esquivar al enemigo, ajustando según el ángulo de detección
            double esquivaAngulo = -_e.getBearing();
            _r.setTurnRight(esquivaAngulo);
            _contador -= 1;
            _r.setAhead(30);
            
        } else {  // Si no hay enemigos cerca o fuera del rango de esquiva
            // Girar hacia el punto de destino
            _r.setTurnRight(anguloRelativo);
        }
        
        if(isTeammate() && _e.getDistance() < 60){
            _r.setTurnRight(-_e.getBearing());
            _r.setBack(20);
        } else if(distancia > 120){
            _r.setAhead(distancia/2);
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
        _e = e;
        _contador = 2;
        
        if(!isTeammate()){
            dispara();
        }
    }
    
    public boolean isTeammate(){
        boolean isTeammate = false;
        if(_e!=null){
            for (String teammate : _r.getTeammates()) {
                if (teammate.equals(_e.getName())) {
                    isTeammate = true;
                }
            }
        }
        return isTeammate;
    }
    
    public static Map<String, Integer> getJerarquia() {
        return jerarquia;
    }

    public static void setJerarquia(Map<String, Integer> nuevaJerarquia) {
        jerarquia = nuevaJerarquia;
    }

    public static String getNextTeamLeader() {
        // Buscar el nivel del líder actual
        Integer currentLevel = jerarquia.get(getTeamLeader());
        if (currentLevel == null) {
            return null; // Si no se encuentra el líder actual, retornar null
        }

        // Buscar el siguiente líder (valor + 1)
        for (int i = currentLevel + 1; i <= 5; i++) { // Cambiar el límite según la jerarquía
            for (Map.Entry<String, Integer> entry : jerarquia.entrySet()) {
                if (entry.getValue() == i) {
                    return entry.getKey(); // Devolver el siguiente líder
                }
            }
        }

        return null; // Si no hay siguiente líder, retornar null
    }
    
    public static String getTeamLeader() {
        // Si no existe, buscar el siguiente valor disponible
        for (int i = 1; i <= 5; i++) { // Cambiar el límite según la jerarquía
            for (Map.Entry<String, Integer> entry : jerarquia.entrySet()) {
                if (entry.getValue() == i) {
                    return entry.getKey(); // Devolver el siguiente líder encontrado
                }
            }
        }

        return null; // Si no hay líderes, retornar null
    }
    
    // Función para obtener el robot anterior en la jerarquía por su nombre
    public static String getPrevious(String currentRobotName) {
        Integer currentValue = jerarquia.get(currentRobotName); // Obtener el valor del robot actual

        if (currentValue == null || currentValue <= 1) {
            return null; // Si el robot no existe o es el primero, no hay anterior
        }

        // Buscar el robot anterior, y seguir buscando si ha muerto
        while (currentValue > 1) {
            currentValue--; // Disminuir el valor para buscar el anterior
            for (Map.Entry<String, Integer> entry : jerarquia.entrySet()) {
                if (Objects.equals(entry.getValue(), currentValue)) {
                    return entry.getKey(); // Devolver el robot anterior activo
                }
            }
        }

        return null; // Si no hay robot anterior activo, retornar null
    }
    
    // Función para eliminar un robot de la jerarquía por su nombre
    public static void removeRobotByName(String robotName) {
        jerarquia.remove(robotName); // Eliminar la entrada del HashMap por nombre
    }
    
    public void enviarCoordenades(){
        try {
            _r.broadcastMessage(new Messages.Position(_r.getName(), _r.getX(), _r.getY()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    // Establecer un nuevo enemigo consensuado
    public static void setEnemy(String enemyName) {
        _enemy = enemyName;
    }

    // Obtener el nombre del enemigo actual
    public static String getCurrentEnemy() {
        return _enemy;
    }

    // Verificar si hay un enemigo activo
    public static boolean hasEnemy() {
        return _enemy != null;
    }

    // Reiniciar enemigo si ha muerto
    public static void resetEnemy() {
        _enemy = null;
    }
    
    public void calcularEnemyPos(){
        // Calcular la posición actual del enemigo
        double angleToEnemy = _r.getHeading() + _e.getBearing();  // Ángulo absoluto
        _enemyX = _r.getX() + Math.sin(Math.toRadians(angleToEnemy)) * _e.getDistance();
        _enemyY = _r.getY() + Math.cos(Math.toRadians(angleToEnemy)) * _e.getDistance();
    }
    
    public void atacarEnemigo() {
        // Ahora podemos predecir la posición futura basándonos en su velocidad y dirección
        double futureX = _enemyX + Math.sin(Math.toRadians(_e.getHeading())) * _e.getVelocity() * 20;
        double futureY = _enemyY + Math.cos(Math.toRadians(_e.getHeading())) * _e.getVelocity() * 20;

        // Calcular el ángulo hacia la posición futura del enemigo
        double anguloHaciaEnemigo = calcularAngulo(_r.getX(), _r.getY(), futureX, futureY);

        // Ajustar el cañón hacia el ángulo del enemigo
        double anguloCañon = normalizarAngulo(anguloHaciaEnemigo - _r.getGunHeading());
        _r.turnGunRight(anguloCañon);

        // Disparar si el cañón está casi alineado con el enemigo
        if (Math.abs(anguloCañon) < 10) {
            double potenciaDisparo = Math.min(3.0, Math.max(1.0, 400 / _e.getDistance()));  // Potencia en función de la distancia
            _r.fire(potenciaDisparo);
        }
    }
    
    public void resetGun(){
        if(!compareDoubles(_r.getHeading(), _r.getGunHeading())){
            _r.setTurnGunRight(_r.getHeading()-_r.getGunHeading());
        }
    }
    
    // Método auxiliar para calcular el ángulo entre dos puntos (igual que antes)
    private double calcularAngulo(double x1, double y1, double x2, double y2) {
        return Math.toDegrees(Math.atan2(x2 - x1, y2 - y1));
    }

}
