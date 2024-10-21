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
 *
 * @author marc
 */
public class EstatTL extends Estat{
    private Point2D.Double[] esquinas;  // Coordenadas de las 4 esquinas
    private int proximaEsquina;         // Índice de la esquina actual
    private final double margen = 0.10; // 10% del campo de batalla
    private final double distanciaCerca = 0; // Tolerancia para saber que llegó a una esquina
    
    public EstatTL(RealStealTeam r) {
        super(r);
        // Cambiamos el color:
        _r.setBodyColor(Color.BLACK);
        _r.setGunColor(Color.RED);
        _r.setRadarColor(Color.RED);
        _r.setScanColor(Color.RED);
        _r.setBulletColor(Color.GREEN);  
        inicializarEsquinas();
        proximaEsquina = calcularEsquinaMasCercana();
    }

    // Inicializar las 4 esquinas del campo de batalla
    private void inicializarEsquinas() {
        double ancho = _r.getBattleFieldWidth();
        double alto = _r.getBattleFieldHeight();
        double margenX = ancho * margen;
        double margenY = alto * margen;

        esquinas = new Point2D.Double[]{
            new Point2D.Double(margenX, margenY),                   // Esquina inferior izquierda
            new Point2D.Double(margenX, alto - margenY),              // Esquina superior izquierda
            new Point2D.Double(ancho - margenX, alto - margenY),        // Esquina superior derecha 
            new Point2D.Double(ancho - margenX, margenY)              // Esquina inferior derecha  
        };
    }
    
    // Calcular la esquina más cercana al TL al inicio
    private int calcularEsquinaMasCercana() {
        double distanciaMin = Double.MAX_VALUE;
        int indiceEsquinaCercana = 0;

        // Buscar la esquina más cercana
        for (int i = 0; i < esquinas.length; i++) {
            double distancia = Point2D.distance(_r.getX(), _r.getY(), esquinas[i].x, esquinas[i].y);
            if (distancia < distanciaMin) {
                distanciaMin = distancia;
                indiceEsquinaCercana = i;
            }
        }

        return indiceEsquinaCercana; // Devuelve el índice de la esquina más cercana
    }
    
    
    @Override
    void torn() {
        Point2D.Double destino = esquinas[proximaEsquina];
        double distancia = _r.getX() - destino.x + _r.getY() - destino.y; 
        // Si estamos cerca de la esquina actual, pasar a la siguiente
        if (_r._logic.compareDoubles(distancia, distanciaCerca, 10)) {
            proximaEsquina = (proximaEsquina + 1) % esquinas.length; // Mover a la siguiente esquina en sentido horario
            System.out.println("proximaEsquina = "+ proximaEsquina);  
        } else {
            // Mover hacia la esquina actual
            _r._logic.moverACoordenadas(destino.x, destino.y);
            _r._logic.escanejar();
        }
         _r._logic.enviarCoordenades();
    }

    @Override
    void onScannedRobot(ScannedRobotEvent e) {
        _r._logic.setObstacle(e);
    }

    @Override
    void onMessageReceived(MessageEvent e) {
        // Si recibe un mensaje de cambio de TL, debe actuar en consecuencia
        if (e.getMessage() instanceof Messages.TeamLeader msg) {
            System.out.println("He recibido un nuevo TL: " + msg.getTlName());
            _r.setEstat(new EstatSeguidor((RealStealTeam) _r));  // Cambiar al estado de seguidor
        }
    }

    @Override
    void onRobotDeath(RobotDeathEvent event) {
        // Si el TL actual muere, asignamos el siguiente TL
        if (event.getName().equals(_r.getName())) {
            System.out.println("El TL ha muerto.");
            
            // Obtener el siguiente en la jerarquía
            String nuevoTL = TeamLogic.getNextTeamLeader();

            if (nuevoTL != null && nuevoTL.equals(_r.getName())) {
                // Este robot se convierte en el nuevo TL
                _r.setEstat(new EstatTL((RealStealTeam) _r));  // Cambiar al estado de TL
                System.out.println("Soy el nuevo TL: " + nuevoTL);
            } else {
                System.out.println("El nuevo TL es otro: " + nuevoTL);
                _r.setEstat(new EstatSeguidor((RealStealTeam) _r));  // Cambiar al estado de seguidor
            }
        }
        TeamLogic.removeRobotByName(event.getName());
    }

    @Override
    void onPaint(Graphics2D g) {
        // Pintamos una circunferencia amarilla alrededor del TL
        int r = 30;
        g.setColor(Color.RED);    
        g.drawOval((int) _r.getX() - r, (int) _r.getY() - r, 2*r, 2*r);
    }
    
    @Override
    void onHitRobot(HitRobotEvent event) {
        _r.setBack(10);
    }
    
}
