/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package practica1;

/**
 *
 * @author mireia
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        if(args.length!=1)
        {
            System.err.println("ERROR: Número de parámetros incorrecto.");
            System.out.println("Uso: Practica1SI2012 fichero_mundo.txt");
            System.out.println("Debes introducir como parámetro un nombre de fichero con la configuración del entorno.");
        }
        else{
            Practica1 practica1 = new Practica1();
            practica1.start(args[0]);
        }
    }

}
