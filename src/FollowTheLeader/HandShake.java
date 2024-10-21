package FollowTheLeader;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import robocode.*;

// Clase principal Handsahe
public class HandShake extends Estat {
    private boolean isTL; // Saber si este robot es el Team Leader
    private String tlName; // Nombre del TL
    private final Map<String, Double> distances = new HashMap<>(); // Almacenar las distancias de cada robot al TL
    private Map<String, Integer> hierarchy = new HashMap<>(); // Almacenar la jerarquía asignada
    private int confirmationsReceived; // Número de confirmaciones de los robots
    
    public HandShake(RealStealTeam r) {
        super(r);
        this.confirmationsReceived = 0;
    }

    @Override
    void torn() {
        // Si aún no se ha definido el TL, iniciamos la elección aleatoria
        if (tlName == null) {
            System.out.println("Eleccion TL: " + Math.random());
            if (Math.random() < 0.01) { // 1% de probabilidad de declararse líder
                isTL = true;
                tlName = _r.getName();
                // Enviamos un mensaje a todos diciendo que este robot es el TL
                try {
                    _r.broadcastMessage(new Messages.TeamLeader(_r.getName()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            // Si ya hay un TL, enviamos nuestra posición al TL
            try {
                if(!isTL){
                    _r._logic.enviarCoordenades();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    void onScannedRobot(ScannedRobotEvent e) {
        // Nada en esta fase
    }

    @Override
    void onMessageReceived(MessageEvent e) {
        if (e.getMessage() instanceof Messages.TeamLeader msg) {
            this.tlName = msg.getTlName(); // Recibimos el nombre del TL
            System.out.println("TL: Elejido" + tlName);
        } else if (e.getMessage() instanceof Messages.Position posMsg) {
            if (isTL) {
                // Si soy el TL, calculo la distancia entre cada robot
                double dist = calcularDistancia(_r.getX(), _r.getY(), posMsg.getX(), posMsg.getY());
                distances.put(posMsg.getSender(), dist);
                System.out.println("Numero de distancias recopiladas: " + distances.size());
                System.out.println("Numero de aliados recopilados: " + _r.getTeammates().length);
                // Cuando se recopilan todas las posiciones, se ordenan
                if (distances.size() == _r.getTeammates().length) {
                    asignarJerarquia();
                    enviarJerarquia();
                }
            }
        } else if (e.getMessage() instanceof Messages.Hierarchy hierarchyMsg) {
            this.hierarchy = hierarchyMsg.getHierarchy(); // Guardamos la jerarquía
            TeamLogic.setJerarquia(hierarchy); // Guardamos la jerarquía globalmente
            // Comprobar si este robot es el nuevo TL
            if (_r.getName().equals(tlName)) {
                isTL = true;          
            }
            
            // Enviamos confirmación de que hemos recibido la jerarquía
            try {
                _r.sendMessage(tlName, new Messages.Confirmation(_r.getName()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
        } else if (e.getMessage() instanceof Messages.Confirmation) {
            // El TL recibe confirmaciones de los robots
            if (isTL) {
                confirmationsReceived++;
                if (confirmationsReceived == _r.getTeammates().length) {
                    // Todos los robots han confirmado, cambiar de estado
                    cambiarEstadoParaTodos();
                }
            }
        } else if (e.getMessage() instanceof Messages.ChangeState) {
            // Cuando un seguidor recibe este mensaje, también cambia de estado
            cambiarEstadoParaTodos(); // Ejecutar el cambio de estado también para seguidores
        }
    }

    @Override
    void onRobotDeath(RobotDeathEvent event) {
        if (event.getName().equals(tlName)) {
            System.out.println("El Team Leader ha muerto. Reorganizando jerarquía...");

            // Obtener el siguiente en la jerarquía
            String nuevoTL = TeamLogic.getNextTeamLeader();

            if (nuevoTL != null) {
                // Asignar al nuevo TL
                tlName = nuevoTL;
                isTL = _r.getName().equals(tlName);

                if (isTL) {
                    // Este robot se convierte en el nuevo TL
                    System.out.println("Soy el nuevo TL: " + tlName);
                    // Reorganizamos la jerarquía para que este robot sea el líder
                    hierarchy.put(tlName, 1); // El nuevo TL es el primero en la jerarquía
                    enviarJerarquia(); // Volvemos a enviar la jerarquía actualizada a todos
                } else {
                    System.out.println("El nuevo TL es: " + tlName);
                }
            }
        }
    }

    @Override
    void onPaint(Graphics2D g) {
        //
    }

    // Método para calcular la distancia entre dos puntos
    private double calcularDistancia(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    // Método para asignar la jerarquía en función de las distancias
    private void asignarJerarquia() {
        // Creamos una lista de entradas del mapa (robot, distancia)
        List<Map.Entry<String, Double>> entries = new ArrayList<>(distances.entrySet());
        // Ordenamos las entradas según la distancia (de menor a mayor)
        Collections.sort(entries, (a, b) -> Double.compare(a.getValue(), b.getValue()));

        // Asignamos la jerarquía: el primero es el TL, luego segundo, tercero, etc.
        hierarchy.put(tlName, 1); // TL es el primero en la jerarquía
        for (int i = 0; i < entries.size(); i++) {
            System.out.println("Posicion de la cola: "+ i + " = " + entries.get(i).getKey());
            hierarchy.put(entries.get(i).getKey(), i + 2); // El resto sigue
        }
    }

    // Método para enviar la jerarquía a todos los robots
    private void enviarJerarquia() {
        try {
            _r.broadcastMessage(new Messages.Hierarchy(hierarchy));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    // Método para cambiar de estado una vez completada la jerarquía
    private void cambiarEstadoParaTodos() {
        try {
            TeamLogic.setJerarquia(hierarchy); // Guardamos la jerarquía globalmente
            // Enviamos un mensaje a todos para cambiar de estado
            _r.broadcastMessage(new Messages.ChangeState());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Cambiamos el estado de este robot
        if (isTL) {
            _r.setEstat(new EstatTL((RealStealTeam) _r));
        } else {
            _r.setEstat(new EstatSeguidor((RealStealTeam) _r));
        }
    }

    @Override
    void onHitRobot(HitRobotEvent event) {
        _r.setBack(10);
    }
}
