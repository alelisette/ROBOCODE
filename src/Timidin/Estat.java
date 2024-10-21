/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Timidin;

import robocode.*;

/**
 * La classe abstracta Estat representa l'estat d'un robot de tipus Timidin.
 * Aquesta classe defineix un conjunt de mètodes abstractes que han de ser 
 * implementats per les subclasses per gestionar els diferents esdeveniments
 * del robot a Robocode.
 * 
 * @author marc
 */
public abstract class Estat {
    /**
     * Referència al robot Timidin associat amb aquest estat.
     */
    protected TimidinRobot _r;
    
    /**
     * Constructor de la classe Estat.
     * Inicialitza la referència al robot associat amb aquest estat.
     * 
     * @param r El robot Timidin que serà associat amb aquest estat.
     */
    public Estat(TimidinRobot r) {
        _r = r;
    }
 
    /**
     * Mètode abstracte que representa les accions que es realitzen en cada torn del joc.
     * Aquest mètode serà implementat per les subclasses per definir el comportament del robot.
     */
    abstract void torn();
    
    /**
     * Mètode abstracte que gestiona l'esdeveniment de detectar un altre robot.
     * Aquest mètode serà implementat per les subclasses per definir les accions
     * a prendre quan es detecta un altre robot.
     * 
     * @param e L'esdeveniment que conté la informació sobre el robot detectat.
     */
    abstract void onScannedRobot(ScannedRobotEvent e);
    
    /**
     * Mètode abstracte que gestiona l'esdeveniment d'impacte amb una paret.
     * Aquest mètode serà implementat per les subclasses per definir les accions
     * a prendre quan el robot impacta contra una paret.
     * 
     * @param e L'esdeveniment que conté la informació sobre l'impacte amb la paret.
     */
    abstract void onHitWall(HitWallEvent e);
    
    /**
     * Mètode abstracte que gestiona l'esdeveniment d'impacte amb un altre robot.
     * Aquest mètode serà implementat per les subclasses per definir les accions
     * a prendre quan el robot col·lideix amb un altre robot.
     * 
     * @param e L'esdeveniment que conté la informació sobre la col·lisió amb l'altre robot.
     */
    abstract void onHitRobot(HitRobotEvent e);
    
    /**
     * Mètode abstracte que gestiona l'esdeveniment de la mort d'un altre robot.
     * Aquest mètode serà implementat per les subclasses per definir les accions
     * a prendre quan un altre robot és destruït.
     * 
     * @param e L'esdeveniment que conté la informació sobre la mort del robot.
     */
    abstract void onRobotDeath(RobotDeathEvent e);
}
