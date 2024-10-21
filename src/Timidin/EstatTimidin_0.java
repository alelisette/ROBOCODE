/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Timidin;

import java.util.List;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import robocode.*;

/**
 * La classe EstatTimidin_0 implementa un estat específic del robot Timidin
 * dins de la competició de Robocode. Aquest estat defineix el comportament
 * del robot en resposta a esdeveniments com la detecció de robots enemics
 * i el moviment cap a una cantonada estratègica.
 * 
 * @author marc
 */
public class EstatTimidin_0 extends Estat {
    
    /**
     * Constructor de la classe EstatTimidin_0.
     * Inicialitza aquest estat associat al robot Timidin.
     * 
     * @param robot El robot Timidin associat amb aquest estat.
     */
    public EstatTimidin_0(TimidinRobot robot) {
        super(robot);
    }
    
    /**
     * Aquest mètode s'executa en cada torn del joc. Comprova si el radar 
     * i el tanc estan centrats, i ajusta l'angle de gir quan cal. Si tot 
     * està centrat, canvia l'estat del robot.
     */
    @Override
    void torn() {
        if (!_r.logic.getGira()) {   
            _r.setTurnRadarRight(45);
        } else if (!_r.logic.getTancCentrat() || !_r.logic.getRadarCentrat()) {
            // Comprova i ajusta l'angle de gir per centrar el tanc
            double angleDeGir = _r.logic.normalitzarAngle(_r.logic.getGraus() - _r.getHeading());
            _r.setTurnRight(angleDeGir);
            if (_r.logic.compareDoubles(_r.getHeading(), _r.logic.getGraus())) {
                _r.logic.setTancCentrat(true);
            }
            // Comprova i ajusta l'angle de gir per centrar el radar
            if (!_r.logic.compareDoubles(_r.getHeading(), _r.getRadarHeading())) {
                _r.setTurnRadarRight(_r.getHeading() - _r.getRadarHeading());
            } else {
                _r.logic.setRadarCentrat(true);
            }
        } else {
            _r.logic.setEstat(new EstatTimidin_1(_r));
        }
    }
    
    /**
     * Gestió de l'esdeveniment onScannedRobot. Quan es detecta un robot enemic,
     * aquest mètode calcula la posició de l'enemic i tria la cantonada més 
     * allunyada del camp de batalla per a dirigir-hi el robot.
     * 
     * @param e L'esdeveniment de detecció d'un robot enemic.
     */
    @Override 
    public void onScannedRobot(ScannedRobotEvent e) {
        if (!_r.logic.getGira()) {
            System.out.println("==================================\n");
            System.out.println("Scanned\n");
            
            // Informació inicial sobre la posició del robot
            double x = _r.getX();
            double y = _r.getY();
            double angleRadar = e.getBearing(); // Angle relatiu a l'enemic
            angleRadar = (_r.getHeading() + angleRadar) % 360; // Angle absolut a l'enemic
            
            if (angleRadar < 0) {
                angleRadar += 360;
            }
            angleRadar = Math.toRadians(angleRadar);
            
            // Calcula la posició de l'enemic
            double distanciaEnemic = e.getDistance();
            double ampleTerreny = _r.getBattleFieldWidth();
            double alturaTerreny = _r.getBattleFieldHeight();
            double xEnemic = x + distanciaEnemic * Math.sin(angleRadar);
            double yEnemic = y + distanciaEnemic * Math.cos(angleRadar);

            // Calculem les cantonades del camp de batalla
            List<Point2D.Double> cantonades = new ArrayList<>();
            cantonades.add(new Point2D.Double(0, 0));                 
            cantonades.add(new Point2D.Double(ampleTerreny, 0));             
            cantonades.add(new Point2D.Double(0, alturaTerreny));              
            cantonades.add(new Point2D.Double(ampleTerreny, alturaTerreny));          

            // Troba la cantonada més allunyada de l'enemic
            Point2D.Double cantonadaMesLluny = cantonades.get(0);
            double maxDistancia = _r.logic.distancia(xEnemic, yEnemic, cantonadaMesLluny.x, cantonadaMesLluny.y);

            for (int i = 1; i < cantonades.size(); i++) {
                Point2D.Double cantonada = cantonades.get(i);
                double dist = _r.logic.distancia(xEnemic, yEnemic, cantonada.x, cantonada.y);

                if (dist > maxDistancia) {
                    maxDistancia = dist;
                    cantonadaMesLluny = cantonada;
                }
            }

            // Calcula l'angle cap a la cantonada més llunyana
            double dx = cantonadaMesLluny.x - _r.getX();
            double dy = cantonadaMesLluny.y - _r.getY();
            double angleCantonada = Math.toDegrees(Math.atan2(dx, dy));
            angleCantonada = (angleCantonada + 360) % 360;

            // Gira el robot cap a la cantonada més allunyada
            _r.logic.setGira(true);
            _r.logic.setGraus(angleCantonada);
            _r.logic.setCantonadaMesLluny(cantonadaMesLluny.x, cantonadaMesLluny.y);
        }
    }

    /**
     * Gestió de l'esdeveniment onHitRobot. Actualment no està implementat.
     * 
     * @param e L'esdeveniment d'impacte amb un altre robot.
     * @throws UnsupportedOperationException Sempre es llança, ja que aquest mètode
     *         encara no està implementat.
     */
    @Override
    void onHitRobot(HitRobotEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Gestió de l'esdeveniment onHitWall. Actualment no està implementat perque no l'utilitzarem .
     * 
     * @param e L'esdeveniment d'impacte amb una paret.
     * @throws UnsupportedOperationException Sempre es llança, ja que aquest mètode
     *         encara no està implementat.
     */
    @Override
    void onHitWall(HitWallEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Gestió de l'esdeveniment onRobotDeath. Actualment no està implementat ja que en aquest estat no el farem servir.
     * 
     * @param e L'esdeveniment de la mort d'un altre robot.
     * @throws UnsupportedOperationException Sempre es llança, ja que aquest mètode
     *         encara no està implementat perque no fa falta en aquest estat.
     */
    @Override
    void onRobotDeath(RobotDeathEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
