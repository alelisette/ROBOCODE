/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package demorobocode;

import java.util.List;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import robocode.Robot;
import robocode.ScannedRobotEvent;

/**
 *
 * @author marc
 */
public class EstatTimidin_0 extends Estat {

    public EstatTimidin_0(TimidinRobot robot) {
        super(robot);
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
        
        //Calculem les distancies de totes les cantonades:
        List<Point2D.Double> cantonades = new ArrayList<>();
        cantonades.add(new Point2D.Double(0, 0));                 
        cantonades.add(new Point2D.Double(ampleTerreny, 0));             
        cantonades.add(new Point2D.Double(0, alturaTerreny));              
        cantonades.add(new Point2D.Double(ampleTerreny, alturaTerreny));          

        Point2D.Double cantonadaMesLluny = cantonades.get(0);
        double maxDistancia = distancia(xEnemic, yEnemic, cantonadaMesLluny.x, cantonadaMesLluny.y);

        int i = 1;
        while (i < cantonades.size()) {
            Point2D.Double contonada = cantonades.get(i);
            double dist = distancia(xEnemic, yEnemic, contonada.x, contonada.y);

            if (dist > maxDistancia) {
                maxDistancia = dist;
                cantonadaMesLluny = contonada;
            }

            i++;
        }

        System.out.println("Altura terreny: " +_r.getBattleFieldHeight());
        System.out.println("Amplada terreny: " +_r.getBattleFieldWidth() + "\n");
        System.out.println("Cantonada més allunyat: x = " + cantonadaMesLluny.x + " y = " + cantonadaMesLluny.y + "\n");
        
        _r.setGira(true); //Aturem el radar
    }
    
    // Método auxiliar para calcular la distancia entre dos puntos
    private double distancia(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}
