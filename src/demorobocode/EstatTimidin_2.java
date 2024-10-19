package demorobocode;

import robocode.*;

public class EstatTimidin_2 extends Estat {
    private ScannedRobotEvent _e;
    
    public EstatTimidin_2(TimidinRobot robot) {
        super(robot);
        System.out.println("ESTAT 2\n");
        _r.setAdjustRadarForGunTurn(true);  // El radar puede girar independientemente del cañón
        _r.setAdjustGunForRobotTurn(true);
        _e = null;
        _r.logic.setRadarCentrat(false);

    }
    
    @Override 
    public void onScannedRobot(ScannedRobotEvent e) {
        if(_e==null){
            _e = e;
        }
    }

    @Override
    void torn() {
        if (_e==null) {
            // Si no se ha escaneado a nadie, busca girando el radar.
            _r.setTurnRadarRight(25);  // Gira el radar para buscar enemigos
        } else if(!_r.logic.getRadarCentrat()) {
            // Centrar el radar sobre el enemigo
            double gunOffset = _r.getHeading() + _e.getBearing() - _r.getGunHeading();

            // Normalizar ángulos para asegurarnos de que sean entre -180 y 180 grados
            gunOffset = _r.logic.normalitzarAngle(gunOffset);

            // Mover el cañón y el radar hacia el enemigo
            _r.setTurnGunRight(gunOffset);            

            dispara();
        }
    }
    
    private void dispara(){
        if (Math.abs(_r.getTurnRemaining()) < 10) {
            // Calcular la potencia de disparo inversamente proporcional a la distancia
            double firePower = Math.min(700 / _e.getDistance(), 3); // Máximo de potencia es 3

            // Asegurar que la potencia mínima sea razonable (ej. 0.1)
            firePower = Math.max(firePower, 0.1);

            System.out.println("SetFire " + firePower + "\n");
            _r.setFire(firePower);
        }
    }

    @Override
    void onHitRobot(HitRobotEvent e) {
        // Comportamiento cuando se choca con otro robot (por ahora, no hacer nada)
    }

    @Override
    void onHitWall(HitWallEvent e) {
        // Comportamiento cuando se golpea contra una pared (por ahora, no hacer nada)
    }

    @Override
    void onRobotDeath(RobotDeathEvent e) {
        if(e.getName().equals(_e.getName())){
            _e = null;
            _r.logic.setRadarCentrat(false);
        }
    }
}
