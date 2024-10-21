/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FollowTheLeader;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import robocode.*;

/**
 * Classe que defineix el comportament d'un robot que actua com a Team Leader (TL). 
 * Aquest robot es mourà entre les cantonades del camp de batalla, mentre coordina
 * i gestiona la comunicació amb la resta de robots seguidors.
 * 
 * @author marc
 */
public class EstatTL extends Estat {
    /** Coordenades de les quatre cantonades del camp de batalla */
    private Point2D.Double[] esquinas; 
    
    /** Índex de la propera cantonada a la qual es desplaçarà el TL */
    private int proximaEsquina; 
    
    /** Mida del marge (percentatge) a les vores del camp de batalla per definir les cantonades */
    private final double margen = 0.10; 
    
    /** Tolerància per determinar si el robot ha arribat a una cantonada */
    private final double distanciaCerca = 0;

    /**
     * Constructor de la classe EstatTL. Inicialitza el TL amb el seu estat i 
     * ajusta els colors i configuracions del radar i canó.
     * 
     * @param r Instància del robot equipat amb la lògica del TL.
     */
    public EstatTL(RealStealTeam r) {
        super(r);
        // Canviem el color del robot TL
        _r.setBodyColor(Color.BLACK);
        _r.setGunColor(Color.RED);
        _r.setRadarColor(Color.RED);
        _r.setScanColor(Color.RED);
        _r.setBulletColor(Color.GREEN);  
        inicializarEsquinas();
        proximaEsquina = calcularEsquinaMasCercana();
        _r._logic.resetGun();
        _r._logic.resetRadar();
        _r.setAdjustGunForRobotTurn(true);
        _r.setAdjustRadarForGunTurn(true);
        _r.setAdjustRadarForRobotTurn(true);
    }

    /**
     * Inicialitza les quatre cantonades del camp de batalla amb coordenades en funció de la mida del camp
     * i ordre a seguir les cantonades.
     */
    private void inicializarEsquinas() {
        double ancho = _r.getBattleFieldWidth();
        double alto = _r.getBattleFieldHeight();
        double margenX = ancho * margen;
        double margenY = alto * margen;
        if(!_r._logic.getCambioRoles()){
            esquinas = new Point2D.Double[]{
                new Point2D.Double(margenX, margenY),                   // Cantonada inferior esquerra
                new Point2D.Double(margenX, alto - margenY),            // Cantonada superior esquerra
                new Point2D.Double(ancho - margenX, alto - margenY),    // Cantonada superior dreta 
                new Point2D.Double(ancho - margenX, margenY)            // Cantonada inferior dreta  
            };
        } else {
            esquinas = new Point2D.Double[]{
                new Point2D.Double(margenX, margenY),                   // Cantonada inferior esquerra
                new Point2D.Double(ancho - margenX, margenY),           // Cantonada inferior dreta
                new Point2D.Double(ancho - margenX, alto - margenY),    // Cantonada superior dreta 
                new Point2D.Double(margenX, alto - margenY)             // Cantonada superior esquerra              
            };
        }
    }
    
    /**
     * Calcula quina cantonada és la més propera al TL en el moment d'iniciar-se l'estratègia.
     * 
     * @return Índex de la cantonada més propera.
     */
    private int calcularEsquinaMasCercana() {
        double distanciaMin = Double.MAX_VALUE;
        int indiceEsquinaCercana = 0;

        // Busca la cantonada més propera
        for (int i = 0; i < esquinas.length; i++) {
            double distancia = Point2D.distance(_r.getX(), _r.getY(), esquinas[i].x, esquinas[i].y);
            if (distancia < distanciaMin) {
                distanciaMin = distancia;
                indiceEsquinaCercana = i;
            }
        }

        return indiceEsquinaCercana; // Retorna l'índex de la cantonada més propera
    }
    
    /**
     * Mètode que es crida en cada torn del robot. El TL es mou cap a la següent cantonada i escaneja
     * l'entorn per a possibles enemics. A més, envia les coordenades a la resta d'unitats.
     */
    @Override
    void torn() {
        _r._logic.invertirJerarquia();
        Point2D.Double destino = esquinas[proximaEsquina];
        double distancia = _r.getX() - destino.x + _r.getY() - destino.y; 
        // Si s'arriba a la cantonada actual, es passa a la següent
        if (_r._logic.compareDoubles(distancia, distanciaCerca, 10)) {
            proximaEsquina = (proximaEsquina + 1) % esquinas.length; // Moure's a la següent cantonada
            System.out.println("proximaEsquina = "+ proximaEsquina);  
        } else {
            // Moure's cap a la cantonada actual
            _r._logic.moverACoordenadas(destino.x, destino.y);
            _r._logic.resetRadar();
            _r._logic.escanejar();
        }
         _r._logic.enviarCoordenades();
    }

    /**
     * Gestiona els esdeveniments quan es detecta un robot escanejat.
     * 
     * @param e L'esdeveniment de robot escanejat.
     */
    @Override
    void onScannedRobot(ScannedRobotEvent e) {
        _r._logic.setObstacle(e);
    }

    /**
     * Gestiona els missatges rebuts pel robot. Si el TL rep la informació que ha de canviar d'estat, 
     * passa a ser seguidor.
     * 
     * @param e L'esdeveniment de missatge rebut.
     */
    @Override
    void onMessageReceived(MessageEvent e) {
        if (e.getMessage() instanceof Messages.TeamLeader msg) {
            System.out.println("He rebut un nou TL: " + msg.getTlName());
            _r.setEstat(new EstatSeguidor((RealStealTeam) _r));  // Canvia a l'estat de seguidor
        } else if(e.getMessage() instanceof Messages.ChangeState){
            if(!TeamLogic.getTeamLeader().equals(_r.getName())){
                _r.setEstat(new EstatSeguidor(_r));
            } 
        }
    }

    /**
     * Gestiona l'esdeveniment quan un robot mor. 
     * Si el TL mor, es reorganitza la jerarquia de l'equip. Si mor un 
     * teammate s'elimina de la jerarquia, i és reorganitza l'equip.L.
     * 
     * @param event L'esdeveniment de mort d'un robot.
     */
    @Override
    void onRobotDeath(RobotDeathEvent event) {
        if (event.getName().equals(_r.getName())) {
            System.out.println("El TL ha mort.");
            
            // Obtenir el següent en la jerarquia
            String nuevoTL = TeamLogic.getNextTeamLeader();

            if (nuevoTL != null && nuevoTL.equals(_r.getName())) {
                // Aquest robot es converteix en el nou TL
                _r.setEstat(new EstatTL((RealStealTeam) _r));  // Canviar a l'estat de TL
                System.out.println("Sóc el nou TL: " + nuevoTL);
            } else {
                System.out.println("El nou TL és un altre: " + nuevoTL);
                _r.setEstat(new EstatSeguidor((RealStealTeam) _r));  // Canviar a l'estat de seguidor
            }
        }
        TeamLogic.removeRobotByName(event.getName());
    }

    /**
     * Dibuixa una circumferència al voltant del TL per identificar-lo visualment en la interfície gràfica.
     * 
     * @param g Context gràfic.
     */
    @Override
    void onPaint(Graphics2D g) {
        int r = 30;
        g.setColor(Color.RED);    
        g.drawOval((int) _r.getX() - r, (int) _r.getY() - r, 2*r, 2*r);
    }

    /**
     * Gestiona l'esdeveniment quan el robot colpeja un altre robot.
     * 
     * @param event L'esdeveniment de col·lisió.
     */
    @Override
    void onHitRobot(HitRobotEvent event) {
        _r.setBack(20);
    }
}
