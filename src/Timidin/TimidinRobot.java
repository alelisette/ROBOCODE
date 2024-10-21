/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Timidin;

import java.awt.Color;
import robocode.*;

/**
 * La classe TimidinRobot és una implementació d'un robot avançat (AdvancedRobot)
 * en Robocode. Utilitza una lògica personalitzada per controlar els seus moviments,
 * el radar, el canó i la resposta als diferents esdeveniments del joc.
 * 
 * El robot es pinta de diferents colors, i implementa una lògica de canvis d'estat 
 * per reaccionar als esdeveniments que ocorren durant la batalla.
 * 
 * @author marc
 */
public class TimidinRobot extends AdvancedRobot {
    /**
     * Instància de la classe LogicaRobot, que gestiona els estats i la lògica interna del robot.
     */
    LogicaRobot logic;

    /**
     * Inicialitza els colors del robot i crea una nova instància de la lògica del robot.
     * Aquest mètode s'utilitza per configurar els aspectes visuals i funcionals del robot.
     */
    private void initTimidinRobot() {
        this.setBodyColor(Color.BLACK);
        this.setGunColor(Color.RED);
        this.setRadarColor(Color.RED);
        this.setScanColor(Color.RED);
        this.setBulletColor(Color.GREEN);
        logic = new LogicaRobot();
    }

    /**
     * Mètode principal del robot, que executa el bucle infinit durant la batalla.
     * El robot comença per inicialitzar-se, després canvia a l'estat inicial
     * i en cada torn executa la lògica de l'estat actual.
     */
    @Override
    public void run() {
        initTimidinRobot();  // Inicialitza el robot
        logic.setEstat(new EstatTimidin_0(this));  // Estableix l'estat inicial
        
        // Bucle infinit del robot
        while (true) {
            logic.getEstat().torn();  // Executa el mètode torn() de l'estat actual
            execute();  // Comanda l'execució del moviment
        }
    }

    /**
     * Gestió de l'esdeveniment onScannedRobot. Quan es detecta un robot enemic,
     * delega el control a l'estat actual per gestionar la detecció de l'enemic.
     * 
     * @param event L'esdeveniment que conté la informació sobre el robot detectat.
     */
    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        logic.getEstat().onScannedRobot(event);
    }

    /**
     * Gestió de l'esdeveniment onHitRobot. Quan el robot col·lideix amb un altre robot,
     * delega el control a l'estat actual per gestionar la col·lisió.
     * 
     * @param event L'esdeveniment de col·lisió amb un altre robot.
     */
    @Override
    public void onHitRobot(HitRobotEvent event) {
        logic.getEstat().onHitRobot(event);
    }

    /**
     * Gestió de l'esdeveniment onHitWall. Quan el robot col·lideix amb una paret,
     * delega el control a l'estat actual per gestionar l'impacte.
     * 
     * @param event L'esdeveniment de col·lisió amb una paret.
     */
    @Override
    public void onHitWall(HitWallEvent event) {
        logic.getEstat().onHitWall(event);
    }

    /**
     * Gestió de l'esdeveniment onRobotDeath. Quan un altre robot mor,
     * delega el control a l'estat actual per gestionar la mort d'un enemic.
     * 
     * @param event L'esdeveniment de la mort d'un altre robot.
     */
    @Override
    public void onRobotDeath(RobotDeathEvent event) {
        logic.getEstat().onRobotDeath(event);
    }
}
    