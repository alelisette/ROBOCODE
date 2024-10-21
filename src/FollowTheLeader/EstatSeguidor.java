/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FollowTheLeader;

import java.awt.Color;
import java.awt.Graphics2D;
import robocode.*;

/**
 * Classe que representa l'estat d'un robot seguidor dins de l'equip.
 * Els robots seguidors segueixen al seu antecessor i ataquen enemics 
 * segons la informació que comparteix l'equip.
 * 
 * @author marc
 */
public class EstatSeguidor extends Estat {
    /** Nom del TL (Team Leader) actual
     * 
     */
    private String tlName;  
    
    /** Nom del robot a seguir
     * 
     */
    private String nameFollow;
    
    /** Coordenades X del robot ha de seguir
     * 
     */
    private double puntX;
    /** Coordenades Y del robot ha de seguir
     * 
     */
    private double puntY;

    /**
     * Constructor de la classe EstatSeguidor. Inicialitza l'estat de seguidor 
     * amb la informació del TL i del robot a seguir.
     * 
     * @param r Referència al robot de l'equip RealStealTeam.
     */
    public EstatSeguidor(RealStealTeam r) {
        super(r);
        /** 
         * TL actual de la jerarquia.
        */
        tlName = TeamLogic.getTeamLeader(); 
        /**
         * Nom del robot a seguir.
         */
        nameFollow = TeamLogic.getPrevious(_r.getName()); 
        /**
         * Posicio X a moures.
         */
        puntX = 0.0;
        /**
         * Posicio Y a moures.
         */
        puntY = 0.0;
                
        // Canviem el color del robot per indicar que és un seguidor
        _r.setBodyColor(Color.ORANGE);
        _r.setGunColor(Color.ORANGE);
        _r.setRadarColor(Color.ORANGE);
        _r.setScanColor(Color.ORANGE);
        _r.setBulletColor(Color.ORANGE);
        
        // Desvincular el canó del radar perquè es moguin de manera independent
        _r.setAdjustGunForRobotTurn(true);
        _r.setAdjustRadarForGunTurn(true);
    }

    /**
     * Lògica que s'executa en cada torn del robot seguidor. Inclou el moviment
     * del robot cap al punt a seguir i l'atac a l'enemic, si n'hi ha un.
     */
    @Override
    void torn() {
        _r._logic.enviarCoordenades();
        if(_r._logic.hasEnemy()){
            _r._logic.atacarEnemigo();
        }
        _r._logic.resetRadar();
        _r._logic.escanejar();
        if(puntX != 0.0){
            _r._logic.moverSeguidorACoordenadas(puntX, puntY);
        }
    }

    /**
     * Mètode que s'executa quan el robot detecta un altre robot amb el radar.
     * Si no existeix enemic conjunt, s'estableix com l'enemic actual. En cas 
     * que existeixi un enemic i el detectat sigui l'enemic actual, enviem la 
     * informació de la ubicació de l'enemic a la resta de teammates.
     */
    @Override
    void onScannedRobot(ScannedRobotEvent e) {
        _r._logic.setObstacle(e);
        if(!_r._logic.isTeammate() && !_r._logic.hasEnemy()){
            _r._logic.setEnemy(e);
            _r._logic.calcularEnemyPos();
            try {
                _r.broadcastMessage(new Messages.EnemyInfoMessage(e));
                _r.broadcastMessage(new Messages.EnemyPositionMessage(_r._logic.getEnemyX(), _r._logic.getEnemyY(),
                        e.getHeading(), e.getVelocity()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if(_r._logic.hasEnemy() && e.getName().equals(_r._logic.getCurrentEnemy().getName())){
            _r._logic.setEnemy(e);
            _r._logic.calcularEnemyPos();
            try {
                _r.broadcastMessage(new Messages.EnemyInfoMessage(e));
                _r.broadcastMessage(new Messages.EnemyPositionMessage(_r._logic.getEnemyX(), _r._logic.getEnemyY(),
                        e.getHeading(), e.getVelocity()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Mètode que s'executa quan el robot rep un missatge d'un altre robot de l'equip.
     * El missatge pot contenir informació sobre el TL, la posició, el contador de segons, 
     * els enemics o jerarquia de l'equip.
     * 
     * @param e Esdeveniment MessageEvent amb el missatge rebut.
     */
    @Override
    void onMessageReceived(MessageEvent e) {
        if (e.getMessage() instanceof Messages.TeamLeader msg) {
            tlName = msg.getTlName(); // Actualitzar el nom del TL

            // Si aquest robot és el nou TL, ha de canviar el seu estat
            if (_r.getName().equals(tlName)) {
                _r.setEstat(new EstatTL((RealStealTeam) _r));  // Canviar a l'estat de TL
            }
        } else if (e.getMessage() instanceof Messages.Position posMsg) {
            if(posMsg.getSender().equals(nameFollow)){
                puntX = posMsg.getX();
                puntY = posMsg.getY();
            }
        } else if(e.getMessage() instanceof Messages.EnemyInfoMessage enemyMsg){
            _r._logic.setEnemy(enemyMsg.getEnemy());
            _r._logic.calcularEnemyPos();
        } else if(e.getMessage() instanceof Messages.EnemyPositionMessage posMsg){
            // Actualitzar la informació de l'enemic amb les dades rebudes
            _r._logic.setEnemyValues(posMsg.getX(), posMsg.getY(), 
                    posMsg.getHeading(), posMsg.getVelocity());        
        } else if (e.getMessage() instanceof Messages.Hierarchy hierarchyMsg) {
            TeamLogic.setJerarquia(hierarchyMsg.getHierarchy()); // Guardar la jerarquia
            _r._logic.switchCambioRoles();
            // Comprovar si aquest robot és el nou TL
            if(TeamLogic.getTeamLeader().equals(_r.getName())){
                _r._logic.setLastRoleChanged(0);
                _r.setEstat(new EstatTL(_r));
            } else {
                nameFollow = TeamLogic.getPrevious(_r.getName());
                tlName = TeamLogic.getTeamLeader();
            }
        } else if (e.getMessage() instanceof Messages.ActualTime timemsg) {
            _r._logic.setLastRoleChanged(timemsg.getTime());
        }
    }

    /**
     * Mètode que s'executa quan un altre robot mor durant la batalla.
     * Si el TL mor, es reorganitza la jerarquia de l'equip. Si mor un 
     * teammate s'elimina de la jerarquia, i és reorganitza l'equip.
     * 
     * @param event Esdeveniment RobotDeathEvent amb la informació del robot mort.
     */
    @Override
    void onRobotDeath(RobotDeathEvent event) {
        System.out.println("Ha mort: " + event.getName());
        // Si el TL mor, reorganitzem la jerarquia
        if (event.getName().equals(tlName)) {
            System.out.println("El TL ha mort.");

            // Obtenir el següent a la jerarquia
            tlName = TeamLogic.getNextTeamLeader();
            
            if (tlName != null && _r.getName().equals(tlName)) {
                // Aquest robot es converteix en el nou TL
                System.out.println("Sóc el nou TL: " + _r.getName());
                _r._logic.resetGun();
                _r.setEstat(new EstatTL((RealStealTeam) _r));  // Canviar a l'estat de TL
            } else {
                System.out.println("El nou TL és: " + tlName);
            }
        } else if(event.getName().equals(_r._logic.getCurrentEnemy().getName())) {
            _r._logic.resetEnemy();
            _r._logic.resetGun();
        }
        TeamLogic.removeRobotByName(event.getName());
        nameFollow = TeamLogic.getPrevious(_r.getName());
    }

    /**
     * Mètode per gestionar quan el robot col·lisiona amb un altre robot.
     * 
     * @param event Esdeveniment HitRobotEvent amb la informació de la col·lisió.
     */
    @Override
    void onHitRobot(HitRobotEvent event) {
        _r.setBack(20);
    }

    /**
     * Mètode per pintar elements gràfics en el camp de batalla.
     * De moment, no s'ha implementat.
     * 
     * @param g L'objecte Graphics2D per dibuixar al camp de batalla.
     */
    @Override
    void onPaint(Graphics2D g) {}
}
