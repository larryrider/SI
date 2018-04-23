package practica1;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mireia
 */

import simbad.sim.*;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import javax.vecmath.Color3f;

public class MiEntorno extends EnvironmentDescription {
    

    private void dibujarMundo(Practica1 practica1){
            float tamCaja = (float)0.5;

            //Indica las luces
            /*******************************************************/
            light1IsOn = true;
            light2IsOn = false;

            //Tamaño del mundo
            double mitadMundo = practica1.tamaño_mundo/2.0;

            //Indica el tamaño del mundo
            setWorldSize(practica1.tamaño_mundo);

            //Dibuja las paredes exteriores
                //Pared inferior
                Wall w1 = new Wall(new Vector3d(mitadMundo, 0, 0), practica1.tamaño_mundo+1, 1, 2, this);
                w1.rotate90(1);
                add(w1);

                //Pared superior
                Wall w2 = new Wall(new Vector3d(-mitadMundo, 0, 0), practica1.tamaño_mundo+1, 1, 2, this);
                w2.rotate90(1);
                add(w2);

                //Pared lateral izquierda
                Wall w3 = new Wall(new Vector3d(0, 0, mitadMundo), practica1.tamaño_mundo, 1, 2, this);
                add(w3);

                //Pared lateral derecha, la dibuja en tres partes para poder hacer el punto de destino
                //Wall w5 = new Wall(new Vector3d(0, 0, -mitadMundo), tamaño_mundo+1, 1, 2, this);
                //add(w5);
                Wall w7 = new Wall(new Vector3d((mitadMundo-(practica1.tamaño_mundo-practica1.destino)/2.0), 0, -mitadMundo), practica1.tamaño_mundo-practica1.destino-1, 1, 2, this);
                add(w7);
                Wall w8 = new Wall(new Vector3d(-(practica1.tamaño_mundo-practica1.destino)/2.0, 0, -mitadMundo), practica1.destino-1, 1, 2, this);
                add(w8);
                //Punto destino
                Wall w6 = new Wall(new Vector3d(-(mitadMundo-practica1.destino),0, -mitadMundo), 1, 1, 2, this);
                w6.setColor(new Color3f(1,0,0));
                add(w6);

            //Añade el robot en el punto indicado como origen
                add(new MiRobot(new Vector3d(-(mitadMundo-practica1.origen-1), 0, 9), "SI robot", practica1));
                
            //Añade los elementos internos del entorno
                //Recorre el mundo para ver los obstáculos
                for(int i=1; i<practica1.tamaño_mundo-1; i++){
                    for(int j=1; j<practica1.tamaño_mundo-1; j++){
                        //Si es un obstáculo, introduce una caja
                        if(practica1.mundo[i][j] == 2){
                            Box b1 = new Box(new Vector3d(i-mitadMundo,0,-(j-mitadMundo)), new Vector3f(1, 1, 1), this);
                            b1.setColor(new Color3f(0,1,1));
                            add(b1);
                        } else{
                            if(practica1.mundo[i][j] == 1){
                                Box b1 = new Box(new Vector3d(i-mitadMundo,0,-(j-mitadMundo)), new Vector3f(1, 2, 1), this);
                                b1.setColor(new Color3f(0,0,1));
                                add(b1);
                            }
                        }

                    }
                }
        }
    
    public MiEntorno(Practica1 practica1) {
        dibujarMundo(practica1);
    }


}
