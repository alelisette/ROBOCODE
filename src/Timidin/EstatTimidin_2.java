
package Timidin;

import robocode.*;

/**
 * La classe EstatTimidin_2 representa un estat en el qual el robot Timidin
 * busca i dispara a enemics en funció de la informació recopilada pel radar.
 * Aquesta classe també gestiona el comportament del robot quan col·lideix amb
 * altres robots o parets.
 * 
 * @author Aleli
 */
public class EstatTimidin_2 extends Estat {
    private ScannedRobotEvent _e;

    /**
     * Constructor de la classe EstatTimidin_2.
     * Inicialitza l'estat del robot, permetent que el radar i el canó girin
     * independentment del cos del robot.
     * 
     * @param robot El robot Timidin associat amb aquest estat.
     */
    public EstatTimidin_2(TimidinRobot robot) {
        super(robot);
        System.out.println("ESTAT 2\n");
        _r.setAdjustRadarForGunTurn(true);  // El radar pot girar independentment del canó
        _r.setAdjustGunForRobotTurn(true);  // El canó pot girar independentment del cos
        _e = null;
        _r.logic.setRadarCentrat(false);

    }

    /**
     * Gestió de l'esdeveniment onScannedRobot. Quan es detecta un robot enemic,
     * emmagatzema la informació del robot escanejat.
     * 
     * @param e L'esdeveniment de detecció d'un robot enemic.
     */
    @Override 
    public void onScannedRobot(ScannedRobotEvent e) {
        if (_e == null) {
            _e = e;
        }
    }

    /**
     * Lògica executada en cada torn. Si no s'ha detectat cap enemic, gira el radar 
     * per buscar. Si hi ha un enemic detectat, centra el radar i el canó sobre ell
     * i dispara.
     */
    @Override
    void torn() {
        if (_e == null) {
            // Si no s'ha escanejat a cap robot, gira el radar per buscar-ne.
            _r.setTurnRadarRight(25);
        } else if (!_r.logic.getRadarCentrat()) {
            // Centrar el radar sobre l'enemic detectat
            double gunOffset = _r.getHeading() + _e.getBearing() - _r.getGunHeading();

            // Normalitza l'angle per assegurar que estigui entre -180 i 180 graus
            gunOffset = _r.logic.normalitzarAngle(gunOffset);

            // Gira el canó cap a l'enemic
            _r.setTurnGunRight(gunOffset);

            dispara();
        }
    }

    /**
     * Mètode privat que dispara al robot enemic detectat. La potència del tret
     * es calcula de forma inversament proporcional a la distància a l'enemic.
     */
    private void dispara() {
        if (Math.abs(_r.getTurnRemaining()) < 10) {
            // Calcula la potència de foc en funció de la distància a l'enemic
            double firePower = Math.min(700 / _e.getDistance(), 3);  // La potència màxima és 3

            // Assegura que la potència mínima sigui almenys 0.1
            firePower = Math.max(firePower, 0.1);

            System.out.println("SetFire " + firePower + "\n");
            _r.setFire(firePower);
        }
    }

    /**
     * Gestió de l'esdeveniment onHitRobot. Per ara, no es realitza cap acció
     * quan el robot col·lideix amb un altre robot.
     * 
     * @param e L'esdeveniment de col·lisió amb un altre robot.
     */
    @Override
    void onHitRobot(HitRobotEvent e) {
        // Comportament quan es col·lideix amb un altre robot (sense acció per ara)
    }

    /**
     * Gestió de l'esdeveniment onHitWall. Per ara, no es realitza cap acció
     * quan el robot col·lideix amb una paret.
     * 
     * @param e L'esdeveniment de col·lisió amb una paret.
     */
    @Override
    void onHitWall(HitWallEvent e) {
        // Comportament quan es colpeja una paret (sense acció per ara)
    }

    /**
     * Gestió de l'esdeveniment onRobotDeath. Si l'enemic escanejat mor, el robot
     * deixa de seguir-lo i comença a buscar nous enemics.
     * 
     * @param e L'esdeveniment de la mort d'un altre robot.
     */
    @Override
    void onRobotDeath(RobotDeathEvent e) {
        if (e.getName().equals(_e.getName())) {
            _e = null;
            _r.logic.setRadarCentrat(false);
        }
    }
}
