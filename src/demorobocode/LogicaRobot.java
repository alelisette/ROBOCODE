/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package demorobocode;

import java.awt.geom.Point2D;

/**
 *
 * @author marc
 */
public class LogicaRobot {
    private Estat _e;
    private boolean _gira;
    private boolean _tancCentrat;
    private boolean _radarCentrat;
    private double _graus;
    private boolean _mou;
    private double _moviment;
    private double _grausDesviacio;
    private boolean _cercaObstaculo;
    //private double _grausRadar;
    private Point2D.Double _cantonadaMesLluny;
    
    public LogicaRobot(){
        _gira = false;
        _tancCentrat = false;
        _radarCentrat = false;
        _graus = 0.0;
        _mou = false;
        _moviment = 0.0;
        _grausDesviacio = 0.0;
        _cercaObstaculo = false;
        _cantonadaMesLluny = new Point2D.Double(0,0);
    }
    
    public void setEstat(Estat nouEstat){
        _e = nouEstat;
    }
    
    public Estat getEstat(){
        return _e;
    }
    
    public boolean getGira(){
        return _gira;
    }
    
    public void setGira(boolean nouEstat){
        _gira = nouEstat;
    }
    
    public void setGraus(double graus){
        _graus = graus;
    }
    
    public double getGraus(){
        return _graus;
    }
    
    public void setMou(boolean nouEstat){
        _mou = nouEstat;
    }
    
    public boolean getMou(){
        return _mou;
    }
    
    public void setTancCentrat(boolean nouEstat){
        _tancCentrat = nouEstat;
    }
    
    public boolean getTancCentrat(){
        return _tancCentrat;
    }
    
    public void setRadarCentrat(boolean nouEstat){
        _radarCentrat = nouEstat;
    }
    
    public boolean getRadarCentrat(){
        return _radarCentrat;
    }
    
    public void setMoviment(double mov){
        _moviment = mov;
    }
    
    public void setGrausDesviacio(double graus){
        _grausDesviacio = graus;
    }
    
    public void setCercaObstaculo(boolean setEstat){
        _cercaObstaculo = setEstat;
    }
    
    public double normalitzarAngle(double angle){
        while (angle > 180) angle -= 360;
        while (angle < -180) angle += 360;
        return angle;
    }
    
    public void setCantonadaMesLluny(double x, double y){
        _cantonadaMesLluny.x = x;
        _cantonadaMesLluny.y = y;
    }
    
    public Point2D.Double getCantonadaMesLluny(){
        return _cantonadaMesLluny;
    }
    
    public void recalcularAngleCantonada(double x, double y, double heading){
        // Calculem l'angle cap a la cantonada més llunyana
        // Calculem l'angle cap a la cantonada més llunyana
        double dx = _cantonadaMesLluny.x - x;
        double dy = _cantonadaMesLluny.y - y;

        // Calcula l'angle absolut cap a la cantonada més llunyana
        double angleCantonada = Math.toDegrees(Math.atan2(dx, dy)); // Angle cap a la cantonada en graus

        // Calcular l'angle relatiu entre l'orientació actual i la cantonada
        double angleRelatiu = angleCantonada - heading;

        // Normalitzar l'angle entre -180 i 180 per girar en la direcció més curta
        angleRelatiu = normalitzarAngle(angleRelatiu);

        // Establir el grau de gir necessari
        this.setGraus(angleRelatiu);
    }
    
    //Comprar dos doubles sense errors de imprecisions.
    public boolean compareDoubles(double num1, double num2, double epsilon){
        //Comprar dos doubles sin errores de inprecision. 
        boolean result = Math.abs(num1-num2) < epsilon;
        return result;
    }
    
    public boolean compareDoubles(double num1, double num2){
        return compareDoubles(num1, num2, 0.1);
    }
    
    // Mètode per calcular la distància entre dos punts
    public double distancia(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}

