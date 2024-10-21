/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FollowTheLeader;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author marc
 */
public class Messages {
    // Clase para el mensaje de confirmación
    public static class Confirmation implements Serializable {
        private String sender;

        public Confirmation(String sender) {
            this.sender = sender;
        }

        public String getSender() {
            return sender;
        }
    }
    
    // Clase para el mensaje de cambio de estado
    public static class ChangeState implements Serializable {
        // Simplemente indica que todos deben cambiar de estado
    }
    
    // Clase para el mensaje de Team Leader
    public static class TeamLeader implements Serializable {
        private String tlName;

        public TeamLeader(String tlName) {
            this.tlName = tlName;
        }

        public String getTlName() {
            return tlName;
        }
    }
    
    // Clase para el mensaje de posiciones
    public static class Position implements Serializable {
        private String sender;
        private double x, y;

        public Position(String sender, double x, double y) {
            this.sender = sender;
            this.x = x;
            this.y = y;
        }

        public String getSender() {
            return sender;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }
    
    // Clase para el mensaje de jerarquía
    public static class Hierarchy implements Serializable {
        private Map<String, Integer> hierarchy;

        public Hierarchy(Map<String, Integer> hierarchy) {
            this.hierarchy = hierarchy;
        }

        public Map<String, Integer> getHierarchy() {
            return hierarchy;
        }
    }
    
    // Mensaje para compartir el enemigo consensuado
    public class EnemyInfoMessage implements Serializable {
        private String enemyName;

        public EnemyInfoMessage(String enemyName) {
            this.enemyName = enemyName;
        }

        public String getEnemyName() {
            return enemyName;
        }
    }
}
