/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package demorobocode;

import java.util.List;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import robocode.*;

/**
 *
 * @author marc
 */
public class EstatTimidin_0 extends Estat {
    
    public EstatTimidin_0(TimidinRobot robot) {
        super(robot);
    }
    
    @Override
    void torn(){
        if(!_r.logic.getGira()){   
            _r.setTurnRadarRight(45);
        } else if(!_r.logic.getTancCentrat() || !_r.logic.getRadarCentrat()){
            //Comprobem si hem girat els suficients graus (centrem el tanc)
            double angleDeGir = _r.logic.normalitzarAngle(_r.logic.getGraus()-_r.getHeading());
            _r.setTurnRight(angleDeGir);
            if(_r.logic.compareDoubles(_r.getHeading(),_r.logic.getGraus())){
                _r.logic.setTancCentrat(true);
            }
            //Comprobem si hem girat els suficients graus (centrem el radar)
            if(!_r.logic.compareDoubles(_r.getHeading(), _r.getRadarHeading())){
                _r.setTurnRadarRight(_r.getHeading()-_r.getRadarHeading());
            } else {
                _r.logic.setRadarCentrat(true);
            } 
        } else {
            _r.logic.setEstat(new EstatTimidin_1(_r));
        }
    }
    
    @Override 
    public void onScannedRobot(ScannedRobotEvent e){
        //Decidim la cantonada sobre la base del primer enemic detectat 
        if(!_r.logic.getGira()){
            System.out.println("==================================\n");
            System.out.println("Scanned\n");
            // Informació inicial
            double x = _r.getX();
            double y = _r.getY();
            double angleRadar = e.getBearing(); // Angle relatiu a l'enemic
            angleRadar = (_r.getHeading()+angleRadar)%360; // Angle absolut a l'enemic
            
            //En cas d'un resultat negatiu, apliquem el modul
            if(angleRadar < 0){
                angleRadar += 360;
            }
            angleRadar = Math.toRadians(angleRadar);
            
            //Calcul posició robot enemic
            double distanciaEnemic = e.getDistance();
            System.out.println("distanciaEnemic: " + distanciaEnemic + "\n");
            double ampleTerreny = _r.getBattleFieldWidth();
            double alturaTerreny = _r.getBattleFieldHeight();
            double xEnemic = x + distanciaEnemic * Math.sin(angleRadar);
            double yEnemic = y + distanciaEnemic * Math.cos(angleRadar);

            System.out.println("xEnemic: " + xEnemic + "\n");
            System.out.println("yEnemic: " + yEnemic + "\n");

            //Calculem totes les cantonades:
            List<Point2D.Double> cantonades = new ArrayList<>();
            cantonades.add(new Point2D.Double(0, 0));                 
            cantonades.add(new Point2D.Double(ampleTerreny, 0));             
            cantonades.add(new Point2D.Double(0, alturaTerreny));              
            cantonades.add(new Point2D.Double(ampleTerreny, alturaTerreny));          

            //Cantonada inicial
            Point2D.Double cantonadaMesLluny = cantonades.get(0);
            double maxDistancia = _r.logic.distancia(xEnemic, yEnemic, cantonadaMesLluny.x, cantonadaMesLluny.y);

            //Comprovem quina és la cantonada més allunyada
            int i = 1;
            while (i < cantonades.size()) {
                Point2D.Double contonada = cantonades.get(i);
                double dist = _r.logic.distancia(xEnemic, yEnemic, contonada.x, contonada.y);

                if (dist > maxDistancia) {
                    maxDistancia = dist;
                    cantonadaMesLluny = contonada;
                }

                i++;
            }

            //Imprimim la informació obtinguda
            System.out.println("Altura terreny: " +_r.getBattleFieldHeight());
            System.out.println("Amplada terreny: " +_r.getBattleFieldWidth() + "\n");
            System.out.println("Cantonada més allunyat: x = " + cantonadaMesLluny.x + " y = " + cantonadaMesLluny.y + "\n");

            // Calculem l'angle cap a la cantonada més llunyana
            double dx = cantonadaMesLluny.x - _r.getX();
            double dy = cantonadaMesLluny.y - _r.getY();
            double angleCantonada = Math.toDegrees(Math.atan2(dx, dy)); // Angle cap a la cantonada en graus

            // Normalitzem l'angle perquè estigui entre 0 i 360 graus
            angleCantonada = (angleCantonada + 360) % 360;
            System.out.println("Angle fins la cantonada més allunyada: " + angleCantonada + "\n");

            // Girem el robot cap a la cantonada més allunyada
            _r.logic.setGira(true);
            _r.logic.setGraus(angleCantonada);
            _r.logic.setCantonadaMesLluny(cantonadaMesLluny.x, cantonadaMesLluny.y);
        }
    }

    @Override
    void onHitRobot(HitRobotEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    void onHitWall(HitWallEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    void onRobotDeath(RobotDeathEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
