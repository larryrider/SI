/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package practica1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import simbad.gui.Simbad;


/**
 *
 * @author mireia
 */
public class Practica1 {

    //Tamaño del mundo
    int tamaño_mundo;
    //Coordenada de inicio del robot
    int origen;
    //Objetivo de llegada del robot
    int destino;
    //Matriz que contiene el mundo
    int mundo[][];
    //Mundo donde se encuentra el robot
    MiEntorno entorno;

    public Practica1(){
        tamaño_mundo = 0;
        origen = 0;
        destino = 0;
        mundo = null;
    }

 
     //Función para leer el fichero
     //Lee un tablero de juego desde un fichero
    public boolean leerMundo(String archivo){
        FileReader fr = null;
        String sCadena;
        String delimitador = " ";
        int i;

        try
            {
                //Abre el fichero
                fr = new FileReader(archivo);
                BufferedReader bf = new BufferedReader(fr);

                try
                    {

                        //Lee la primera línea del archivo que indica el tamaño del mundo
                        if((sCadena = bf.readLine()) != null){
                            sCadena = sCadena.trim();
                            tamaño_mundo = Integer.parseInt(sCadena);

                            //Crea el mundo del tamaño especificado
                            mundo = new int[tamaño_mundo][tamaño_mundo];
                        }else{
                            System.err.println("ERROR. Formato del fichero incorrecto");
                            return false;
                        }

                        //Lee la coordenada de origen
                        if((sCadena = bf.readLine()) != null){
                            sCadena = sCadena.trim();
                            origen = Integer.parseInt(sCadena);
                        }else{
                            System.err.println("ERROR. Formato del fichero incorrecto");
                            return false;
                        }

                        //Lee la coordenada de destino
                        if((sCadena = bf.readLine()) != null){
                            sCadena = sCadena.trim();
                            destino = Integer.parseInt(sCadena);
                        }else{
                            System.err.println("ERROR. Formato del fichero incorrecto");
                            return false;
                        }

                       //Lee el mundo
                        i = 0;
                        while (i<tamaño_mundo)
                        {
                            if((sCadena = bf.readLine())!=null){
                                //Separa los diferentes elementos de la cadena que ha leído
                                String[] elementos = sCadena.split(delimitador);

                                for (int j = 0; j < elementos.length; j++) {
                                    if(elementos[j].equals("."))
                                        mundo[i][j] = 0;
                                    else if(elementos[j].equals("-"))
                                            mundo[i][j] = 2;
                                         else
                                            mundo[i][j] = 1;
                                }

                                i++;
                            }else{
                                System.err.println("ERROR. Formato del fichero incorrecto");
                                return false;
                            }
                        }

                    } catch (IOException e1)
                    {
                        System.err.println("Error en la lectura del fichero:"+archivo);
                        return false;
                    }
                //Si falla la apertura del fichero
            } catch (FileNotFoundException e2)
            {
                System.err.println("Error al abrir el fichero: "+archivo);
                return false;
            }finally{
            // Cerramos el fichero en finally porque así nos aseguramos que se cierra tanto si todo ha ido bien, como
            // si ha saltado alguna excepción
            try{
                if( null != fr )
                    {
                        fr.close();
                    }
            }catch (Exception e3){
                System.err.println("Error al cerrar el fichero: "+archivo);

            }
        }
            return true;
    }

    void start(String entrada){

            if(leerMundo(entrada)){
                //Si la lectura del fichero ha sido correcta, crea una instancia del simulador
                //Solicita antialising
                System.setProperty("j3d.implicitAntialiasing", "true");
                //Crea la instancia de Simbad con el entorno
                entorno = new MiEntorno(this);
                Simbad frame = new Simbad(entorno, false);

                //Muestra por pantalla el mundo que lee desde fichero
                /*for (int i=0; i<tamaño_mundo; i++){
                    for(int j=0;j<tamaño_mundo; j++)
                        System.out.print(mundo[i][j]+" ");
                    System.out.println();
                }*/
            }else{
                System.out.println("ERROR EN EL FICHERO");
            }
      }

}
