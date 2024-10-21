/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FollowTheLeader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import robocode.ScannedRobotEvent;

/**
 *
 * @author marc
 */
public class TeamLogic {
    private final RealStealTeam _r;
    private ScannedRobotEvent _obstacle;
    private ScannedRobotEvent _enemy;
    private double _enemyX;
    private double _enemyY;
    private double _enemyHeading;
    private double _enemyVelocity;
    private static Map<String, Integer> jerarquia = new HashMap<>(); // Jerarquía global
    private boolean _scanIzquierda;
    private boolean _scanDerecha;
    private boolean _scanCentro;
    private int _contador = 0;
    private boolean _cambioRoles;
    private long lastRoleChangeTime; // Guardará el valor del último turno en que se cambió el rol

   
    public TeamLogic(RealStealTeam r){
        _r = r;
        _enemy = null;
        _enemyX = 0.0;
        _enemyY = 0.0;
        _enemyHeading = 0.0;
        _enemyVelocity = 0.0;
        _scanIzquierda = true;
        _scanDerecha = false;
        _scanCentro = false;
        _contador = 0;
        _cambioRoles = false;
        lastRoleChangeTime = 0;
    }
    
    public void setLastRoleChanged(long time){
        lastRoleChangeTime = time;
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
        if (_obstacle != null && _obstacle.getDistance() < 250.0 && _contador!=0) {  // Si hay un enemigo cerca
            
            if (_obstacle.getDistance() < 120) {
                _r.setBack(50);  // Retroceder si el enemigo está demasiado cerca
            } 

            // Girar para esquivar al enemigo, ajustando según el ángulo de detección
            double esquivaAngulo = -_obstacle.getBearing();
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
        if (_obstacle != null && _obstacle.getDistance() < 250.0 && _contador!=0 && !isTeammate()) {  // Si hay un enemigo cerca
            
            if (_obstacle.getDistance() < 120) {
                _r.setBack(100);  // Retroceder si el enemigo está demasiado cerca
            } 

            // Girar para esquivar al enemigo, ajustando según el ángulo de detección
            double esquivaAngulo = -_obstacle.getBearing();
            _r.setTurnRight(esquivaAngulo);
            _contador -= 1;
            _r.setAhead(50);
            
        } else {  // Si no hay enemigos cerca o fuera del rango de esquiva
            // Girar hacia el punto de destino
            _r.setTurnRight(anguloRelativo);
        }
        
        if(isTeammate() && _obstacle.getDistance() < 60){
            _r.setTurnRight(-_obstacle.getBearing());
            _r.setBack(20);
        } else if(distancia > 120){
            _r.setAhead(distancia/2);
        }
    }
        
    public void dispara(){
        calcularEnemyPos();
        atacarEnemigo();
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
        if(compareDoubles(_r.getHeading(),_r.getRadarHeading())){
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
    
    public void setObstacle(ScannedRobotEvent e){
        _obstacle = e;
        _contador = 2;
        
        if(!isTeammate()){
            _enemy = _obstacle;
            dispara();
        }
    }
    
    public boolean isTeammate(){
        boolean isTeammate = false;
        if(_obstacle!=null){
            for (String teammate : _r.getTeammates()) {
                if (teammate.equals(_obstacle.getName())) {
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
    
    public void invertirJerarquia() {
        if(lastRoleChangeTime==0){
            lastRoleChangeTime = System.currentTimeMillis();
            // Enviamos el timepo actual
            try {
                _r.broadcastMessage(new Messages.ActualTime(lastRoleChangeTime));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("Timpo actual: " + (System.currentTimeMillis() - lastRoleChangeTime) + " ms");
        if (System.currentTimeMillis() - lastRoleChangeTime >= 15000) { // Cada 15 segundos
            _cambioRoles = !_cambioRoles;

            // Crear una lista de los robots en orden jerárquico
            List<Map.Entry<String, Integer>> listaJerarquia = new ArrayList<>(jerarquia.entrySet());

            // Ordenar la lista por el valor de la jerarquía (posición en el equipo)
            listaJerarquia.sort(Map.Entry.comparingByValue());

            // Invertir la lista de jerarquía
            Collections.reverse(listaJerarquia);

            // Asignar los nuevos valores de jerarquía
            for (int i = 0; i < listaJerarquia.size(); i++) {
                System.out.println("Posicion " + i + ": " + listaJerarquia.get(i).getKey());
                jerarquia.put(listaJerarquia.get(i).getKey(), i + 1);
            }
            
            // Notificar a los demás robots de la nueva jerarquía
            TeamLogic.setJerarquia(jerarquia); // Guardamos la jerarquía globalmente
            try {
                _r.broadcastMessage(new Messages.Hierarchy(jerarquia));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if(!TeamLogic.getTeamLeader().equals(_r.getName())){
                _r.setEstat(new EstatSeguidor(_r));
            }
        }
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
    public void setEnemy(ScannedRobotEvent enemy) {
        _enemy = enemy;
    }

    // Obtener el nombre del enemigo actual
    public ScannedRobotEvent getCurrentEnemy() {
        return _enemy;
    }

    // Verificar si hay un enemigo activo
    public boolean hasEnemy() {
        return _enemy != null;
    }

    // Reiniciar enemigo si ha muerto
    public void resetEnemy() {
        _enemy = null;
    }
    
    public void setEnemyValues(double x, double y, double heading, double speed){
        _enemyX = x;
        _enemyY = y;
        _enemyHeading = heading;
        _enemyVelocity = speed;
    }
    
    public double getEnemyX(){
        return _enemyX;
    }
    
    public double getEnemyY(){
        return _enemyY;
    }
    
    public double getEnemyHeading(){
        return _enemyHeading;
    }
    
    public double getEnemyVelocity(){
        return _enemyVelocity;
    }
    
    public void calcularEnemyPos(){
        if(_enemy!=null){
            // Calcular la posición actual del enemigo
            double angleToEnemy = _r.getHeading() + _enemy.getBearing();  // Ángulo absoluto
            _enemyX = _r.getX() + Math.sin(Math.toRadians(angleToEnemy)) * _enemy.getDistance();
            _enemyY = _r.getY() + Math.cos(Math.toRadians(angleToEnemy)) * _enemy.getDistance();
            _enemyHeading = _enemy.getHeading();
            _enemyVelocity = _enemy.getVelocity();
        }
    }
    
    public void atacarEnemigo() {
        if(_enemy!=null){            
            double futureX = _enemyX + Math.sin(Math.toRadians(_enemyHeading)) * _enemyVelocity * 20;
            double futureY = _enemyY + Math.cos(Math.toRadians(_enemyHeading)) * _enemyVelocity * 20;

            // Calcular el ángulo hacia la posición futura del enemigo
            double anguloHaciaEnemigo = calcularAngulo(_r.getX(), _r.getY(), futureX, futureY);

            // Ajustar el cañón hacia el ángulo del enemigo
            double anguloCañon = normalizarAngulo(anguloHaciaEnemigo - _r.getGunHeading());
            _r.setTurnGunRight(anguloCañon);

            double distancia = distancia(_r.getX(), _r.getY(), futureX, futureY);
            // Disparar si el cañón está casi alineado con el enemigo
            if (Math.abs(anguloCañon) < 10) {
                double potenciaDisparo = Math.min(3.0, Math.max(1.0, 400 / distancia));  // Potencia en función de la distancia
                _r.fire(potenciaDisparo);
            }
        }
    }
    
    public void resetGun(){
        _r.setTurnGunRight(_r.getHeading()-_r.getGunHeading());
    }
    
    public void resetRadar(){
        _r.setTurnRadarRight(_r.getHeading()-_r.getRadarHeading());
    }
    
    // Método auxiliar para calcular el ángulo entre dos puntos (igual que antes)
    private double calcularAngulo(double x1, double y1, double x2, double y2) {
        return Math.toDegrees(Math.atan2(x2 - x1, y2 - y1));
    }
    
    // Mètode per calcular la distància entre dos punts
    public double distancia(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
    
    public boolean getCambioRoles(){
        return _cambioRoles;
    }
    
    public void setCambioRoles(boolean nouEstat){
        _cambioRoles = nouEstat;
    }
    
}
