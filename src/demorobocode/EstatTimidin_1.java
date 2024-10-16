/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package demorobocode;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import robocode.ScannedRobotEvent;

/**
 *
 * @author marc
 */
public class EstatTimidin_1 extends Estat {
    public EstatTimidin_1(TimidinRobot robot) {
        super(robot);
        System.out.println("NOU ESTAT\n");
    }
    
    @Override 
    public void onScannedRobot(ScannedRobotEvent e){
        
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
        System.out.println("angleRadar: " + angleRadar + "\n");
        angleRadar = Math.toRadians(angleRadar);
        double distanciaEnemic = e.getDistance();
        System.out.println("distanciaEnemic: " + distanciaEnemic + "\n");
        double ampleTerreny = _r.getBattleFieldWidth();
        double alturaTerreny = _r.getBattleFieldHeight();
                
        //Calcul posició robot enemic
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
        double maxDistancia = _r.distancia(xEnemic, yEnemic, cantonadaMesLluny.x, cantonadaMesLluny.y);

        //Comprovem quina és la cantonada més allunyada
        int i = 1;
        while (i < cantonades.size()) {
            Point2D.Double contonada = cantonades.get(i);
            double dist = _r.distancia(xEnemic, yEnemic, contonada.x, contonada.y);

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
        _r.setGira(true);
        _r.setGraus(angleCantonada);
    }
    
}
