/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FollowTheLeader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import robocode.ScannedRobotEvent;

/**
 * La classe TeamLogic gestiona la lògica d'equip per un equip de robots, 
 * incloent l'estructura de jerarquia, la coordinació 
 * de moviments, l'atac d'enemics i l'esquiva d'obstacles. 
 * Aquesta classe treballa amb informació compartida entre els robots de l'equip, 
 * utilitzant comunicació per missatgeria.
 * 
 * @author marc
 */
public class TeamLogic {
    /**
    * Equip RealStealTeam associat a aquesta lògica d'equip.
    */
   private final RealStealTeam _r;

   /**
    * Darrer robot escanejat considerat com a obstacle.
    */
   private ScannedRobotEvent _obstacle;

   /**
    * Darrer robot escanejat considerat com a enemic.
    */
   private ScannedRobotEvent _enemy;

   /**
    * Coordenada X de la posició actual de l'enemic.
    */
   private double _enemyX;

   /**
    * Coordenada Y de la posició actual de l'enemic.
    */
   private double _enemyY;

   /**
    * Direcció (en graus) en què es desplaça l'enemic.
    */
   private double _enemyHeading;

   /**
    * Velocitat actual de l'enemic.
    */
   private double _enemyVelocity;

   /**
    * Jerarquia global de l'equip, que defineix el nivell de cada robot.
    */
   private static Map<String, Integer> jerarquia = new HashMap<>();

   /**
    * Indicador de si el radar ha d'escanejar a l'esquerra.
    */
   private boolean _scanIzquierda;

   /**
    * Indicador de si el radar ha d'escanejar a la dreta.
    */
   private boolean _scanDerecha;

   /**
    * Indicador de si el radar ha d'escanejar al centre.
    */
   private boolean _scanCentro;

   /**
    * Comptador per gestionar el temps d'esquiva d'obstacles.
    */
   private int _contador = 0;

   /**
    * Indicador de si s'han intercanviat els rols en l'equip.
    */
   private boolean _cambioRoles;

   /**
    * Temps del darrer canvi de rol, guardat com a mil·lisegons.
    */
   private long lastRoleChangeTime; 


   /**
     * Crea una instància de la classe TeamLogic associada a un equip de robots.
     * Inicialitza les variables internes necessàries, com ara el robot actual,
     * enemics i obstacles detectats, i la jerarquia de l'equip.
     * 
     * @param r L'objecte RealStealTeam que representa l'equip de robots.
     */
    public TeamLogic(RealStealTeam r){
        _r = r;
        _enemy = null;
        _enemyX = 0.0;
        _enemyY = 0.0;
        _enemyHeading = 0.0;
        _enemyVelocity = 0.0;
        _scanIzquierda = true;
        _scanDerecha = false;
        _scanCentro = false;
        _contador = 0;
        _cambioRoles = false;
        lastRoleChangeTime = 0;
    }
    
    /**
     * Estableix el temps de l'últim canvi de rol dins l'equip.
     * 
     * @param time El temps en mil·lisegons del darrer canvi de rol.
     */
    public void setLastRoleChanged(long time){
        lastRoleChangeTime = time;
    }
    
    /**
     * Mou el robot cap a unes coordenades específiques, evitant obstacles si cal.
     * 
     * @param destinoX Coordenada X de la destinació.
     * @param destinoY Coordenada Y de la destinació.
     */
    public void moverACoordenadas(double destinoX, double destinoY) {
        
        // Posición actual del robot
        double xActual = _r.getX();
        double yActual = _r.getY();

        // Calcular la distancia al punto de destino
        double distancia = Math.hypot(destinoX - xActual, destinoY - yActual);

        // Calcular el ángulo absoluto hacia el punto de destino
        double anguloDestino = Math.toDegrees(Math.atan2(destinoX - xActual, destinoY - yActual));

        // Convertir el ángulo del destino a un ángulo relativo respecto a la orientación actual del robot
        double anguloRelativo = normalizarAngulo(anguloDestino - _r.getHeading());

        // Lógica de esquiva de enemigos u obstáculos
        if (_obstacle != null && _obstacle.getDistance() < 250.0 && _contador!=0) {  // Si hay un enemigo cerca
            
            if (_obstacle.getDistance() < 120) {
                _r.setBack(50);  // Retroceder si el enemigo está demasiado cerca
            } else {
                _r.setAhead(30);
            } 
            // Girar para esquivar al enemigo, ajustando según el ángulo de detección
            double esquivaAngulo = -_obstacle.getBearing();
            _r.setTurnRight(esquivaAngulo);
            _contador -= 1;
            
        } else {  // Si no hay enemigos cerca o fuera del rango de esquiva
            // Girar hacia el punto de destino
            _r.setTurnRight(anguloRelativo);
        }
        
        // Solo moverse hacia adelante si el giro se ha completado
        if (compareDoubles(anguloRelativo, 0.0)) {
            _r.setAhead(distancia);
        } 
    }
    
    /**
     * Mou el robot seguidor cap a unes coordenades, evitant obstacles i altres robots de l'equip.
     * 
     * @param destinoX Coordenada X de la destinació.
     * @param destinoY Coordenada Y de la destinació.
     */
    public void moverSeguidorACoordenadas(double destinoX, double destinoY) {
                
        // Posición actual del robot
        double xActual = _r.getX();
        double yActual = _r.getY();

        // Calcular la distancia al punto de destino
        double distancia = Math.hypot(destinoX - xActual, destinoY - yActual);

        // Calcular el ángulo absoluto hacia el punto de destino
        double anguloDestino = Math.toDegrees(Math.atan2(destinoX - xActual, destinoY - yActual));

        // Convertir el ángulo del destino a un ángulo relativo respecto a la orientación actual del robot
        double anguloRelativo = normalizarAngulo(anguloDestino - _r.getHeading());

        // Lógica de esquiva de enemigos u obstáculos
        if (_obstacle != null && _obstacle.getDistance() < 250.0 && _contador!=0 && !isTeammate()) {  // Si hay un enemigo cerca
            
            if (_obstacle.getDistance() < 120) {
                _r.setBack(100);  // Retroceder si el enemigo está demasiado cerca
            } else {
                _r.setAhead(50);

            }

            // Girar para esquivar al enemigo, ajustando según el ángulo de detección
            double esquivaAngulo = -_obstacle.getBearing();
            _r.setTurnRight(esquivaAngulo);
            _contador -= 1;            
        } else {  // Si no hay enemigos cerca o fuera del rango de esquiva
            // Girar hacia el punto de destino
            _r.setTurnRight(anguloRelativo);
        }
        
        if(isTeammate() && _obstacle.getDistance() < 60){
            _r.setTurnRight(-_obstacle.getBearing());
            _r.setBack(20);
        } else if(distancia > 120){
            _r.setAhead(distancia/2);
        }
    }
    
    /**
     * Ordena al TL disparar cap a l'enemic trobat.
     */    
    public void dispara(){
        calcularEnemyPos();
        atacarEnemigo();
    }

    /**
     * Normalitza un angle perquè estigui dins del rang [-180, 180] graus.
     * 
     * @param angulo L'angle a normalitzar.
     * @return L'angle normalitzat.
     */
    public double normalizarAngulo(double angulo) {
        while (angulo > 180) {
            angulo -= 360;
        }
        while (angulo < -180) {
            angulo += 360;
        }
        return angulo;
    }
    
    /**
     * Executa el cicle d'escaneig de l'entorn per detectar obstacles o enemics.
     */
    public void escanejar(){
        // Escaneamos obstaculos:
        if(compareDoubles(_r.getHeading(),_r.getRadarHeading())){
            if (_scanIzquierda) {
                // Gira 22.5 grados hacia la izquierda desde la posición actual
                _r.setTurnRadarLeft(15);
                _scanIzquierda = false;  // Ja hem escanejat l'esquerra
                _scanCentro = true;      // Ara tornem al centre
            } else if (_scanCentro) {
                // Gira 45 graus a la dreta per a cobrir el centre i la dreta
                _r.setTurnRadarRight(30);
                _scanCentro = false;    // Ja hem escanejat el centre
                _scanDerecha = true;    // Ara anem a la dreta
            } else if (_scanDerecha) {
                // Gira 22.5 graus a l'esquerra per a tornar al centre
                _r.setTurnRadarLeft(15);
                _scanDerecha = false;  
                _scanIzquierda = true;  // Tornem a l'esquerra per a reiniciar el cicle
            }
        }
    }
    
    /**
     * Compara dos números de tipus double, tenint en compte una tolerància d'error.
     * 
     * @param num1 Primer número a comparar.
     * @param num2 Segon número a comparar.
     * @param epsilon El marge d'error permès.
     * @return True si els números són iguals dins del marge d'error, false altrament.
     */
    public boolean compareDoubles(double num1, double num2, double epsilon){
        //Comprar dos doubles sin errores de inprecision. 
        boolean result = Math.abs(num1-num2) < epsilon;
        return result;
    }
    
    /**
     * Compara dos números de tipus double, tenint en compte una tolerància d'error per defecte.
     * 
     * @param num1 Primer número a comparar.
     * @param num2 Segon número a comparar.
     * @return True si els números són iguals dins del marge d'error, false altrament.
     */
    public boolean compareDoubles(double num1, double num2){
        return compareDoubles(num1, num2, 0.1);
    }
    
    /**
     * Estableix un obstacle detectat durant l'escaneig i, si no és un company d'equip, 
     * el considera com a enemic.
     * 
     * @param e L'event de robot escanejat (ScannedRobotEvent).
     */
    public void setObstacle(ScannedRobotEvent e){
        _obstacle = e;
        _contador = 2;
        
        if(!isTeammate()){
            _enemy = _obstacle;
            dispara();
        }
    }
    
    /**
     * Determina si l'obstacle actual és un company d'equip.
     * 
     * @return True si l'obstacle és un company d'equip, false altrament.
     */
    public boolean isTeammate(){
        boolean isTeammate = false;
        if(_obstacle!=null){
            for (String teammate : _r.getTeammates()) {
                if (teammate.equals(_obstacle.getName())) {
                    isTeammate = true;
                }
            }
        } 
        return isTeammate;
    }
    
    /**
     * Obté la jerarquia actual dels membres de l'equip.
     * 
     * @return Un mapa que conté la jerarquia dels robots.
     */
    public static Map<String, Integer> getJerarquia() {
        return jerarquia;
    }

    
    /**
     * Estableix una nova jerarquia per a l'equip.
     * 
     * @param nuevaJerarquia El mapa que conté la nova jerarquia dels robots.
     */
    public static void setJerarquia(Map<String, Integer> nuevaJerarquia) {
        jerarquia = nuevaJerarquia;
    }
    
    /**
     * Inverteix la jerarquia actual de l'equip si ha passat prou temps des de l'últim canvi de rol.
     */
    public void invertirJerarquia() {
        if(lastRoleChangeTime==0){
            lastRoleChangeTime = System.currentTimeMillis();
            // Enviamos el timepo actual
            try {
                _r.broadcastMessage(new Messages.ActualTime(lastRoleChangeTime));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("Timpo actual: " + (System.currentTimeMillis() - lastRoleChangeTime) + " ms");
        if (System.currentTimeMillis() - lastRoleChangeTime >= 15000) { // Cada 15 segundos
            _cambioRoles = !_cambioRoles;

            // Crear una lista de los robots en orden jerárquico
            List<Map.Entry<String, Integer>> listaJerarquia = new ArrayList<>(jerarquia.entrySet());

            // Ordenar la lista por el valor de la jerarquía (posición en el equipo)
            listaJerarquia.sort(Map.Entry.comparingByValue());

            // Invertir la lista de jerarquía
            Collections.reverse(listaJerarquia);

            // Asignar los nuevos valores de jerarquía
            for (int i = 0; i < listaJerarquia.size(); i++) {
                System.out.println("Posicion " + i + ": " + listaJerarquia.get(i).getKey());
                jerarquia.put(listaJerarquia.get(i).getKey(), i + 1);
            }
            
            // Notificar a los demás robots de la nueva jerarquía
            TeamLogic.setJerarquia(jerarquia); // Guardamos la jerarquía globalmente
            try {
                _r.broadcastMessage(new Messages.Hierarchy(jerarquia));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if(!TeamLogic.getTeamLeader().equals(_r.getName())){
                _r.setEstat(new EstatSeguidor(_r));
            }
        }
    }

    /**
     * Obté el següent líder de l'equip segons la jerarquia.
     * 
     * @return El nom del següent líder de l'equip.
     */
    public static String getNextTeamLeader() {
        // Buscar el nivel del líder actual
        Integer currentLevel = jerarquia.get(getTeamLeader());
        if (currentLevel == null) {
            return null; // Si no se encuentra el líder actual, retornar null
        }

        // Buscar el siguiente líder (valor + 1)
        for (int i = currentLevel + 1; i <= 5; i++) { // Cambiar el límite según la jerarquía
            for (Map.Entry<String, Integer> entry : jerarquia.entrySet()) {
                if (entry.getValue() == i) {
                    return entry.getKey(); // Devolver el siguiente líder
                }
            }
        }

        return null; // Si no hay siguiente líder, retornar null
    }
    
    /**
     * Obté l'actual líder de l'equip.
     * 
     * @return El nom del líder de l'equip.
     */
    public static String getTeamLeader() {
        // Si no existe, buscar el siguiente valor disponible
        for (int i = 1; i <= 5; i++) { // Cambiar el límite según la jerarquía
            for (Map.Entry<String, Integer> entry : jerarquia.entrySet()) {
                if (entry.getValue() == i) {
                    return entry.getKey(); // Devolver el siguiente líder encontrado
                }
            }
        }

        return null; // Si no hay líderes, retornar null
    }
    
    /**
     * Retorna el nom del robot antecessor a <code>currentRobotName</code>, en cas de no 
     * tenir retorna null
     * 
     * @param currentRobotName El nom del robot actual.
     */
    public static String getPrevious(String currentRobotName) {
        Integer currentValue = jerarquia.get(currentRobotName); // Obtener el valor del robot actual

        if (currentValue == null || currentValue <= 1) {
            return null; // Si el robot no existe o es el primero, no hay anterior
        }

        // Buscar el robot anterior, y seguir buscando si ha muerto
        while (currentValue > 1) {
            currentValue--; // Disminuir el valor para buscar el anterior
            for (Map.Entry<String, Integer> entry : jerarquia.entrySet()) {
                if (Objects.equals(entry.getValue(), currentValue)) {
                    return entry.getKey(); // Devolver el robot anterior activo
                }
            }
        }

        return null; // Si no hay robot anterior activo, retornar null
    }
    
    /**
     * Elimina un robot de la jerarquia per nom.
     * 
     * @param robotName El nom del robot que s'ha d'eliminar.
     */
    public static void removeRobotByName(String robotName) {
        jerarquia.remove(robotName); // Eliminar la entrada del HashMap por nombre
    }
    
    /**
     * Envia les coordenades actuals del robot als altres membres de l'equip.
     */
    public void enviarCoordenades(){
        try {
            _r.broadcastMessage(new Messages.Position(_r.getName(), _r.getX(), _r.getY()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Estableix un nou enemic per a l'equip.
     * 
     * @param enemy L'enemic detectat (ScannedRobotEvent).
     */
    public void setEnemy(ScannedRobotEvent enemy) {
        _enemy = enemy;
    }

     /**
     * Obté l'enemic actual detectat per l'equip.
     * 
     * @return L'enemic actual (ScannedRobotEvent).
     */
    public ScannedRobotEvent getCurrentEnemy() {
        return _enemy;
    }

    /**
     * Verifica si hi ha un enemic actiu.
     * 
     * @return True si hi ha un enemic actiu, false altrament.
     */
    public boolean hasEnemy() {
        return _enemy != null;
    }

    /**
     * Reinicia l'enemic actual (estableix l'enemic a null).
     */
    public void resetEnemy() {
        _enemy = null;
    }
    
    /**
    * Estableix els valors de posició i moviment de l'enemic.
    * 
    * @param x Coordenada X de l'enemic.
    * @param y Coordenada Y de l'enemic.
    * @param heading Direcció de l'enemic en graus.
    * @param speed Velocitat de l'enemic.
    */
    public void setEnemyValues(double x, double y, double heading, double speed){
        _enemyX = x;
        _enemyY = y;
        _enemyHeading = heading;
        _enemyVelocity = speed;
    }
    
    /**
    * Retorna la coordenada X actual de l'enemic.
    * 
    * @return Coordenada X de l'enemic.
    */
    public double getEnemyX(){
        return _enemyX;
    }
    
    /**
    * Retorna la coordenada Y actual de l'enemic.
    * 
    * @return Coordenada Y de l'enemic.
    */
    public double getEnemyY(){
        return _enemyY;
    }
    
    /**
    * Retorna la direcció (en graus) de l'enemic.
    * 
    * @return Direcció de l'enemic en graus.
    */
    public double getEnemyHeading(){
        return _enemyHeading;
    }
    
    /**
    * Retorna la velocitat actual de l'enemic.
    * 
    * @return Velocitat de l'enemic.
    */
    public double getEnemyVelocity(){
        return _enemyVelocity;
    }
    
    /**
    * Calcula la posició actual de l'enemic basant-se en les dades obtingudes de l'escaneig.
    */
    public void calcularEnemyPos(){
        if(_enemy!=null){
            // Calcular la posición actual del enemigo
            double angleToEnemy = _r.getHeading() + _enemy.getBearing();  // Ángulo absoluto
            _enemyX = _r.getX() + Math.sin(Math.toRadians(angleToEnemy)) * _enemy.getDistance();
            _enemyY = _r.getY() + Math.cos(Math.toRadians(angleToEnemy)) * _enemy.getDistance();
            _enemyHeading = _enemy.getHeading();
            _enemyVelocity = _enemy.getVelocity();
        }
    }
    
    /**
     * Ataca l'enemic calculant la seva posició futura i ajustant el canó del robot.
     */
    public void atacarEnemigo() {
        if(_enemy!=null){            
            double futureX = _enemyX + Math.sin(Math.toRadians(_enemyHeading)) * _enemyVelocity * 20;
            double futureY = _enemyY + Math.cos(Math.toRadians(_enemyHeading)) * _enemyVelocity * 20;

            // Calcular el ángulo hacia la posición futura del enemigo
            double anguloHaciaEnemigo = calcularAngulo(_r.getX(), _r.getY(), futureX, futureY);

            // Ajustar el cañón hacia el ángulo del enemigo
            double anguloCañon = normalizarAngulo(anguloHaciaEnemigo - _r.getGunHeading());
            _r.setTurnGunRight(anguloCañon);

            double distancia = distancia(_r.getX(), _r.getY(), futureX, futureY);
            // Disparar si el cañón está casi alineado con el enemigo
            if (Math.abs(anguloCañon) < 10) {
                double potenciaDisparo = Math.min(3.0, Math.max(1.0, 400 / distancia));  // Potencia en función de la distancia
                _r.fire(potenciaDisparo);
            }
        }
    }
    
    /**
     * Reajusta el canó del robot perquè apunti a la seva orientació original.
     */
    public void resetGun(){
        _r.setTurnGunRight(_r.getHeading()-_r.getGunHeading());
    }
    
    /**
     * Reajusta el radar del robot perquè coincideixi amb la seva orientació original.
     */
    public void resetRadar(){
        _r.setTurnRadarRight(_r.getHeading()-_r.getRadarHeading());
    }
    
    /**
     * Calcula l'angle entre dos punts de coordenades.
     * 
     * @param x1 Coordenada X del primer punt.
     * @param y1 Coordenada Y del primer punt.
     * @param x2 Coordenada X del segon punt.
     * @param y2 Coordenada Y del segon punt.
     * @return L'angle entre els dos punts.
     */
    private double calcularAngulo(double x1, double y1, double x2, double y2) {
        return Math.toDegrees(Math.atan2(x2 - x1, y2 - y1));
    }
    
    /**
     * Calcula la distància entre dos punts de coordenades.
     * 
     * @param x1 Coordenada X del primer punt.
     * @param y1 Coordenada Y del primer punt.
     * @param x2 Coordenada X del segon punt.
     * @param y2 Coordenada Y del segon punt.
     * @return La distància entre els dos punts.
     */
    public double distancia(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
    
    /**
     * Verifica si s'ha realitzat un canvi de rols dins l'equip.
     * 
     * @return True si hi ha hagut un canvi de rols, false altrament.
     */
    public boolean getCambioRoles(){
        return _cambioRoles;
    }
    
    public void switchCambioRoles(){
        _cambioRoles = !_cambioRoles;
    }
    
     /**
     * Estableix l'estat del canvi de rols.
     * 
     * @param nouEstat El nou estat del canvi de rols.
     */
    public void setCambioRoles(boolean nouEstat){
        _cambioRoles = nouEstat;
    }
    
}
