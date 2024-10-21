/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FollowTheLeader;

import java.io.Serializable;
import java.util.Map;
import robocode.ScannedRobotEvent;

/**
 * Classe Messages conté totes les classes internes que defineixen diferents
 * tipus de missatges que els robots poden enviar-se entre ells dins l'equip.
 * Inclou missatges per confirmar accions, compartir posicions, jerarquia, 
 * informació de l'enemic, entre d'altres.
 * 
 * @author marc
 */
public class Messages {
    
    /**
     * Classe per a enviar missatges de confirmació entre robots.
     * Aquest missatge s'envia quan un robot confirma la recepció d'una informació.
     */
    public static class Confirmation implements Serializable {
        private String sender;

        /**
         * Constructor que crea un missatge de confirmació.
         * 
         * @param sender El nom del robot que envia la confirmació.
         */
        public Confirmation(String sender) {
            this.sender = sender;
        }

        /**
         * Retorna el nom del robot que ha enviat la confirmació.
         * 
         * @return El nom del robot que ha enviat el missatge.
         */
        public String getSender() {
            return sender;
        }
    }
    
    /**
     * Classe per a enviar un missatge de canvi d'estat.
     * Indica a tots els robots que han de canviar l'estat actual.
     */
    public static class ChangeState implements Serializable {
        // Simplement indica que tots els robots han de canviar d'estat
    }
    
    /**
     * Classe per a enviar un missatge de Team Leader (TL).
     * Aquest missatge es fa servir per informar sobre qui és el TL.
     */
    public static class TeamLeader implements Serializable {
        private String tlName;

        /**
         * Constructor que crea un missatge indicant el Team Leader.
         * 
         * @param tlName El nom del robot que és el TL.
         */
        public TeamLeader(String tlName) {
            this.tlName = tlName;
        }

        /**
         * Retorna el nom del Team Leader.
         * 
         * @return El nom del TL.
         */
        public String getTlName() {
            return tlName;
        }
    }
    
    /**
     * Clase per detectar si més d'un robot es proclama TL, i tornar a
     * fer el procés d'elecció.
     */
    public static class ErrorHandShake implements Serializable {

    }
    
    /**
     * Classe per a enviar missatges amb la posició d'un robot.
     * Aquest missatge conté les coordenades X i Y del robot emissor.
     */
    public static class Position implements Serializable {
        private String sender;
        private double x, y;

        /**
         * Constructor que crea un missatge amb la posició d'un robot.
         * 
         * @param sender El nom del robot que envia la posició.
         * @param x La coordenada X de la posició del robot.
         * @param y La coordenada Y de la posició del robot.
         */
        public Position(String sender, double x, double y) {
            this.sender = sender;
            this.x = x;
            this.y = y;
        }

        /**
         * Retorna el nom del robot que envia la posició.
         * 
         * @return El nom del robot.
         */
        public String getSender() {
            return sender;
        }

        /**
         * Retorna la coordenada X de la posició del robot.
         * 
         * @return La coordenada X.
         */
        public double getX() {
            return x;
        }

        /**
         * Retorna la coordenada Y de la posició del robot.
         * 
         * @return La coordenada Y.
         */
        public double getY() {
            return y;
        }
    }
    
    /**
     * Classe per a enviar la jerarquia entre els robots de l'equip.
     * Aquesta jerarquia defineix l'ordre d'importància o acció dels robots.
     */
    public static class Hierarchy implements Serializable {
        private Map<String, Integer> hierarchy;

        /**
         * Constructor que crea un missatge amb la jerarquia dels robots.
         * 
         * @param hierarchy Un mapa que defineix la jerarquia (nom del robot i posició).
         */
        public Hierarchy(Map<String, Integer> hierarchy) {
            this.hierarchy = hierarchy;
        }

        /**
         * Retorna el mapa amb la jerarquia assignada als robots.
         * 
         * @return La jerarquia de l'equip.
         */
        public Map<String, Integer> getHierarchy() {
            return hierarchy;
        }
    }
    
    /**
     * Classe per a enviar informació sobre un enemic detectat.
     * El missatge inclou l'esdeveniment ScannedRobotEvent que conté 
     * les dades de l'enemic (nom, posició, etc.).
     */
    public static class EnemyInfoMessage implements Serializable {
        ScannedRobotEvent enemy;

        /**
         * Constructor que crea un missatge amb la informació de l'enemic detectat.
         * 
         * @param e L'esdeveniment de l'enemic escanejat (ScannedRobotEvent).
         */
        public EnemyInfoMessage(ScannedRobotEvent e) {
            this.enemy = e;
        }

        /**
         * Retorna el nom de l'enemic detectat.
         * 
         * @return El nom de l'enemic.
         */
        public String getEnemyName() {
            return enemy.getName();
        }
        
        /**
         * Retorna l'objecte ScannedRobotEvent amb la informació completa de l'enemic.
         * 
         * @return L'esdeveniment de l'enemic escanejat.
         */
        public ScannedRobotEvent getEnemy() {
            return enemy;
        }
    }
    
    /**
     * Classe per a enviar la posició d'un enemic.
     * Inclou les coordenades X, Y, així com la seva direcció i velocitat.
     */
    public static class EnemyPositionMessage implements Serializable {
        private double x, y, heading, velocity;

        /**
         * Constructor que crea un missatge amb la posició i moviment de l'enemic.
         * 
         * @param x La coordenada X de l'enemic.
         * @param y La coordenada Y de l'enemic.
         * @param heading La direcció de l'enemic.
         * @param velocity La velocitat de l'enemic.
         */
        public EnemyPositionMessage(double x, double y, double heading, double velocity) {
            this.x = x;
            this.y = y;
            this.heading = heading;
            this.velocity = velocity;
        }

        /**
         * Retorna la coordenada X de l'enemic.
         * 
         * @return La coordenada X.
         */
        public double getX() {
            return x;
        }

        /**
         * Retorna la coordenada Y de l'enemic.
         * 
         * @return La coordenada Y.
         */
        public double getY() {
            return y;
        }

        /**
         * Retorna la direcció (heading) de l'enemic.
         * 
         * @return La direcció de l'enemic.
         */
        public double getHeading() {
            return heading;
        }

        /**
         * Retorna la velocitat de l'enemic.
         * 
         * @return La velocitat de l'enemic.
         */
        public double getVelocity() {
            return velocity;
        }
    }
    
    /**
     * Classe per a enviar l'hora o temps actual.
     * S'utilitza per a sincronitzar els robots amb un mateix moment en el temps.
     */
    public static class ActualTime implements Serializable {
        private long Time;
        
        /**
         * Constructor que crea un missatge amb el temps actual.
         * 
         * @param actualTime El temps actual en mil·lisegons.
         */
        public ActualTime(long actualTime){
            Time = actualTime;
        }
        
        /**
         * Retorna el temps actual que s'ha enviat.
         * 
         * @return El temps en mil·lisegons.
         */
        public long getTime(){
            return Time;
        }
    }
}
