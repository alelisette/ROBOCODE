/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FollowTheLeader;

import java.awt.Color;
import java.awt.Graphics2D;
import robocode.*;

/**
 *
 * @author marc
 */
public class EstatSeguidor extends Estat {
    private String tlName;  // Nombre del TL
    private String nameFollow; //Nom del robot a seguir
    private double puntX; // Coordenada X a seguir
    private double puntY; //Coordenada Y a seguir
    

    public EstatSeguidor(RealStealTeam r) {
        super(r);
        tlName = TeamLogic.getTeamLeader(); // Obtener el TL actual de la jerarquía
        nameFollow = TeamLogic.getPrevious(_r.getName());
        puntX = 0.0;
        puntY = 0.0;
        System.out.println("El TL es: " + tlName);
        // Cambiamos el color:
        _r.setBodyColor(Color.ORANGE);
        _r.setGunColor(Color.ORANGE);
        _r.setRadarColor(Color.ORANGE);
        _r.setScanColor(Color.ORANGE);
        _r.setBulletColor(Color.ORANGE);
    }

    @Override
    void torn() {
        _r._logic.enviarCoordenades();
        if(_r._logic.hasEnemy()){
            _r._logic.atacarEnemigo();
        } else {
            _r._logic.resetGun();
            _r._logic.escanejar();
        }
        if(puntX!=0.0){
            _r._logic.moverSeguidorACoordenadas(puntX, puntY);
        }
    }

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

    @Override
    void onMessageReceived(MessageEvent e) {
        if (e.getMessage() instanceof Messages.TeamLeader msg) {
            tlName = msg.getTlName(); // Actualizar el nombre del TL

            // Si este robot es el nuevo TL, debe cambiar su estado
            if (_r.getName().equals(tlName)) {
                _r.setEstat(new EstatTL((RealStealTeam) _r));  // Cambiar al estado de TL
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
            // Actualizar la información del enemigo con los datos recibidos
            _r._logic.setEnemyValues(posMsg.getX(), posMsg.getY(), 
                    posMsg.getHeading(), posMsg.getVelocity());        
        } else if(e.getMessage() instanceof Messages.ChangeState){
            if(TeamLogic.getTeamLeader().equals(_r.getName())){
                _r.setEstat(new EstatTL(_r));
            } else {
                nameFollow = TeamLogic.getPrevious(_r.getName());
                tlName = TeamLogic.getTeamLeader();
            }
        }
    }

    @Override
    void onRobotDeath(RobotDeathEvent event) {
        System.out.println("Ha muerto: " + event.getName());
        // Si el TL muere, reorganizamos la jerarquía
        if (event.getName().equals(tlName)) {
            System.out.println("El TL ha muerto.");

            // Obtener el siguiente en la jerarquía
            tlName = TeamLogic.getNextTeamLeader();
            
            if (tlName != null && _r.getName().equals(tlName)) {
                // Este robot se convierte en el nuevo TL
                System.out.println("Soy el nuevo TL: " + _r.getName());
                _r._logic.resetGun();
                _r.setEstat(new EstatTL((RealStealTeam) _r));  // Cambiar al estado de TL
            } else {
                System.out.println("El nuevo TL es: " + tlName);
            }
        } else if(event.getName().equals(_r._logic.getCurrentEnemy().getName())) {
            _r._logic.resetEnemy();
            _r._logic.resetGun();
        }
        TeamLogic.removeRobotByName(event.getName());
        nameFollow = TeamLogic.getPrevious(_r.getName());
    }

    @Override
    void onPaint(Graphics2D g) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    void onHitRobot(HitRobotEvent event) {
        _r.setBack(10);
    }
}
