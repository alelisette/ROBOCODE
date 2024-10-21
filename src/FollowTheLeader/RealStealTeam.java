/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FollowTheLeader;

import java.awt.Graphics2D;
import robocode.TeamRobot;
import robocode.*;

/**
 * Classe principal del robot equipat que implementa l'estratègia de 
 * "Follow the Leader" en un entorn de Robocode. El robot actua en equip
 * i es comunica amb altres robots mitjançant missatges. Utilitza un estat 
 * per gestionar el seu comportament.
 * 
 * @author marc
 */
public class RealStealTeam extends TeamRobot {
    private Estat _estat; // Estat actual del robot
    protected TeamLogic _logic; // Lògica del comportament d'equip

    /**
     * Mètode principal del robot que s'executa contínuament. Inicialitza l'estat 
     * a "HandShake" i crea una nova instància de TeamLogic per gestionar la lògica 
     * de l'equip. En cada torn, delega el control a l'estat actual.
     */
    @Override
    public void run() {
        _estat = new HandShake(this);  // Inicialització de l'estat
        _logic = new TeamLogic(this);  // Inicialització de la lògica d'equip
        while (true) {
            _estat.torn();  // Executa les accions definides per l'estat actual
            execute();      // Informa el motor de Robocode sobre les accions a fer
        }
    }

    /**
     * Mètode que es crida quan es rep un missatge d'un altre robot de l'equip.
     * El control es delega a l'estat actual.
     * 
     * @param e Esdeveniment que conté el missatge rebut.
     */
    @Override
    public void onMessageReceived(MessageEvent e) {
        _estat.onMessageReceived(e); // Delegació a l'estat
    }

    /**
     * Mètode que es crida per dibuixar informació extra al radar. El control
     * es delega a l'estat actual, que pot implementar dibuixos personalitzats.
     * 
     * @param g L'objecte Graphics2D que permet dibuixar al radar.
     */
    @Override
    public void onPaint(Graphics2D g) {
        _estat.onPaint(g); // Delegació a l'estat
    }

    /**
     * Mètode que es crida quan es detecta un altre robot amb el radar. El control
     * es delega a l'estat actual per determinar la resposta adequada.
     * 
     * @param event Esdeveniment que conté la informació del robot detectat.
     */
    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        _estat.onScannedRobot(event); // Delegació a l'estat
    }

    /**
     * Mètode que es crida quan es detecta la mort d'un altre robot. El control
     * es delega a l'estat actual per gestionar aquest esdeveniment.
     * 
     * @param event Esdeveniment que conté la informació del robot mort.
     */
    @Override
    public void onRobotDeath(RobotDeathEvent event) {
        _estat.onRobotDeath(event); // Delegació a l'estat
    }

    /**
     * Mètode que es crida quan el robot col·lideix amb un altre robot. El control
     * es delega a l'estat actual per gestionar la col·lisió.
     * 
     * @param event Esdeveniment que conté la informació de la col·lisió.
     */
    @Override
    public void onHitRobot(HitRobotEvent event) {
        _estat.onHitRobot(event); // Delegació a l'estat
    }

    /**
     * Estableix un nou estat per al robot. Aquest mètode permet canviar 
     * dinàmicament el comportament del robot durant el joc.
     * 
     * @param nouEstat El nou estat que es vol assignar al robot.
     */
    public void setEstat(Estat nouEstat) {
        _estat = nouEstat; // Actualització de l'estat
    }
}
