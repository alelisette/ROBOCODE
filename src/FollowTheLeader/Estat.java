/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FollowTheLeader;

import java.awt.Graphics2D;
import robocode.*;

/**
 * Classe abstracta que defineix l'estat en què es troba un robot en el sistema de 
 * seguidors i líder. Cada estat haurà d'implementar els mètodes per gestionar 
 * diferents esdeveniments de Robocode.
 * 
 * @author marc
 */
public abstract class Estat {
    /** Referència al robot de l'equip.
     * 
     */
    RealStealTeam _r;

    /**
     * Constructor de la classe Estat.
     * 
     * @param r Robot de l'equip RealStealTeam que es vol assignar a aquest estat.
     */
    public Estat(RealStealTeam r) {
        _r = r;
    }

    /**
     * Mètode abstracte que defineix el comportament del robot en el seu torn.
     * Cada estat haurà de definir la seva lògica durant el torn del robot.
     */
    abstract void torn();

    /**
     * Mètode abstracte que es crida quan el robot detecta un altre robot.
     * 
     * @param e L'esdeveniment ScannedRobotEvent amb informació del robot detectat.
     */
    abstract void onScannedRobot(ScannedRobotEvent e);

    /**
     * Mètode abstracte que es crida quan el robot rep un missatge.
     * 
     * @param e L'esdeveniment MessageEvent amb la informació del missatge rebut.
     */
    abstract void onMessageReceived(MessageEvent e);

    /**
     * Mètode abstracte que es crida quan un altre robot mor durant la batalla.
     * 
     * @param event L'esdeveniment RobotDeathEvent amb la informació del robot mort.
     */
    abstract void onRobotDeath(RobotDeathEvent event);

    /**
     * Mètode abstracte que es crida quan el robot xoca amb un altre robot.
     * 
     * @param event L'esdeveniment HitRobotEvent amb la informació de la col·lisió.
     */
    abstract void onHitRobot(HitRobotEvent event);

    /**
     * Mètode abstracte que es crida per pintar elements gràfics durant la batalla.
     * 
     * @param g L'objecte Graphics2D utilitzat per dibuixar al camp de batalla.
     */
    abstract void onPaint(Graphics2D g);
}
