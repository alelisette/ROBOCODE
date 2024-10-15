/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package demorobocode;

import robocode.ScannedRobotEvent;

/**
 *
 * @author marc
 */
public class EstatTimidin_1 extends Estat {
    public EstatTimidin_1(TimidinRobot robot) {
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
        
        //Calculem les distancies de totes les esquines:
        double dist1 = distancia(xEnemic, yEnemic, 0.0f, 0.0f);
        double dist2 = distancia(xEnemic, yEnemic, 0.0f, alturaTerreny);
        double dist3 = distancia(xEnemic, yEnemic, ampleTerreny, 0.0f);
        double dist4 = distancia(xEnemic, yEnemic, ampleTerreny, alturaTerreny);
        
        // Escollim la més alluny
        double xEscollida = 0.0f;
        double yEscollida = 0.0f;
        double maxDist = dist1;

        // Cambiar por un while
        if (dist2 > maxDist) {
            yEscollida = alturaTerreny;
            maxDist = dist2;
        }
        if (dist3 > maxDist) {
            xEscollida = ampleTerreny;
            yEscollida = 0.0f;
            maxDist = dist3;
        }
        if (dist4 > maxDist) {
            xEscollida = ampleTerreny;
            yEscollida = alturaTerreny;
            maxDist = dist4;
        }
        
        System.out.println("Altura terreny: " +_r.getBattleFieldHeight());
        System.out.println("Amplada terreny: " +_r.getBattleFieldWidth() + "\n");
        System.out.println("Cantonada més allunyat: x = " + xEscollida + " y = " + yEscollida + "\n");
        
        //Calculem la direcció:
        double angleDeGir = Math.atan2(xEscollida-_r.getX(), yEscollida-_r.getY());
        
        //Apliquem els canvis
        _r.setMou(true);
        _r.setMoviment(xEscollida-_r.getX(), yEscollida-_r.getY());
        _r.setGira(true);
        _r.setGraus(angleDeGir-_r.getHeading());
    }
    
    // Método auxiliar para calcular la distancia entre dos puntos
    private double distancia(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}
