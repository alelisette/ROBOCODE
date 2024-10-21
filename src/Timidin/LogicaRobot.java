/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Timidin;

import java.awt.geom.Point2D;

/**
 * La classe LogicaRobot gestiona la lògica interna del robot, incloent-hi el moviment,
 * el control de l'estat actual, la gestió dels angles i les cantonades, i altres aspectes
 * de la lògica de decisió del robot en funció del seu entorn.
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
    private final Point2D.Double _cantonadaMesLluny;

    /**
     * Constructor de la classe LogicaRobot.
     * Inicialitza els valors de control de l'estat, moviment, angles i cantonades.
     */
    public LogicaRobot() {
        _gira = false;
        _tancCentrat = false;
        _radarCentrat = false;
        _graus = 0.0;
        _mou = false;
        _cantonadaMesLluny = new Point2D.Double(0, 0);
    }

    /**
     * Estableix el nou estat del robot.
     * 
     * @param nouEstat El nou estat a establir.
     */
    public void setEstat(Estat nouEstat) {
        _e = nouEstat;
    }

    /**
     * Obté l'estat actual del robot.
     * 
     * @return L'estat actual.
     */
    public Estat getEstat() {
        return _e;
    }

    /**
     * Determina si el robot està girant.
     * 
     * @return True si el robot està girant, false altrament.
     */
    public boolean getGira() {
        return _gira;
    }

    /**
     * Estableix si el robot ha de girar.
     * 
     * @param nouEstat El nou estat de gir del robot.
     */
    public void setGira(boolean nouEstat) {
        _gira = nouEstat;
    }

    /**
     * Estableix l'angle de gir necessari.
     * 
     * @param graus L'angle de gir a establir.
     */
    public void setGraus(double graus) {
        _graus = graus;
    }

    /**
     * Obté l'angle de gir actual.
     * 
     * @return L'angle de gir en graus.
     */
    public double getGraus() {
        return _graus;
    }

    /**
     * Estableix si el robot està en moviment.
     * 
     * @param nouEstat El nou estat de moviment.
     */
    public void setMou(boolean nouEstat) {
        _mou = nouEstat;
    }

    /**
     * Determina si el robot està en moviment.
     * 
     * @return True si el robot està en moviment, false altrament.
     */
    public boolean getMou() {
        return _mou;
    }

    /**
     * Estableix si el tanc del robot està centrat.
     * 
     * @param nouEstat El nou estat del tanc centrat.
     */
    public void setTancCentrat(boolean nouEstat) {
        _tancCentrat = nouEstat;
    }

    /**
     * Determina si el tanc del robot està centrat.
     * 
     * @return True si el tanc està centrat, false altrament.
     */
    public boolean getTancCentrat() {
        return _tancCentrat;
    }

    /**
     * Estableix si el radar del robot està centrat.
     * 
     * @param nouEstat El nou estat del radar centrat.
     */
    public void setRadarCentrat(boolean nouEstat) {
        _radarCentrat = nouEstat;
    }

    /**
     * Determina si el radar del robot està centrat.
     * 
     * @return True si el radar està centrat, false altrament.
     */
    public boolean getRadarCentrat() {
        return _radarCentrat;
    }

    /**
     * Normalitza un angle perquè estigui entre -180 i 180 graus.
     * 
     * @param angle L'angle a normalitzar.
     * @return L'angle normalitzat.
     */
    public double normalitzarAngle(double angle) {
        while (angle > 180) angle -= 360;
        while (angle < -180) angle += 360;
        return angle;
    }

    /**
     * Estableix la cantonada més llunyana del camp de batalla.
     * 
     * @param x La coordenada X de la cantonada més llunyana.
     * @param y La coordenada Y de la cantonada més llunyana.
     */
    public void setCantonadaMesLluny(double x, double y) {
        _cantonadaMesLluny.x = x;
        _cantonadaMesLluny.y = y;
    }

    /**
     * Obté la cantonada més llunyana del camp de batalla.
     * 
     * @return Les coordenades de la cantonada més llunyana.
     */
    public Point2D.Double getCantonadaMesLluny() {
        return _cantonadaMesLluny;
    }

    /**
     * Recalcula l'angle entre el robot i la cantonada més llunyana.
     * 
     * @param x La coordenada X del robot.
     * @param y La coordenada Y del robot.
     * @param heading L'orientació actual del robot.
     */
    public void recalcularAngleCantonada(double x, double y, double heading) {
        double dx = _cantonadaMesLluny.x - x;
        double dy = _cantonadaMesLluny.y - y;
        double angleCantonada = Math.toDegrees(Math.atan2(dx, dy));
        double angleRelatiu = angleCantonada - heading;
        angleRelatiu = normalitzarAngle(angleRelatiu);
        this.setGraus(angleRelatiu);
    }

    /**
     * Compara dos valors de tipus double per veure si són aproximadament iguals,
     * amb un marge d'error determinat.
     * 
     * @param num1 El primer valor.
     * @param num2 El segon valor.
     * @param epsilon El marge d'error.
     * @return True si els dos valors són iguals dins del marge d'error, false altrament.
     */
    public boolean compareDoubles(double num1, double num2, double epsilon) {
        return Math.abs(num1 - num2) < epsilon;
    }

    /**
     * Compara dos valors de tipus double amb un marge d'error per defecte de 0.1.
     * 
     * @param num1 El primer valor.
     * @param num2 El segon valor.
     * @return True si els dos valors són iguals dins del marge d'error, false altrament.
     */
    public boolean compareDoubles(double num1, double num2) {
        return compareDoubles(num1, num2, 0.1);
    }

    /**
     * Calcula la distància entre dos punts al camp de batalla.
     * 
     * @param x1 La coordenada X del primer punt.
     * @param y1 La coordenada Y del primer punt.
     * @param x2 La coordenada X del segon punt.
     * @param y2 La coordenada Y del segon punt.
     * @return La distància entre els dos punts.
     */
    public double distancia(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}
