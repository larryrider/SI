/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica2;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.simple.*;
import org.json.simple.parser.*;
import javax.swing.JOptionPane;

/**
 *
 * @author larry
 */
public class Adaboost {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Integer opcion;
        do{
            String respuesta = JOptionPane.showInputDialog("Menu adaboost:\n1.Entrenar adaboost\n2.Resultados test\n3.Salir\n\n");
            if(respuesta == null){
                System.exit(0);
                opcion = 3;
            }else if(respuesta.contentEquals("")){
                opcion = 1; //por defecto
            }else{
                try{
                    opcion = Integer.parseInt(respuesta);
                    switch(opcion){
                        case 1:
                            Integer trainingPercent = preguntarCantidad("Introduzca el porcentaje de imágenes a usar para entrenar (0-100):\n");
                            Integer clasificadoresDebiles = preguntarCantidad("Introduzca el número de clasificadores débiles que van a formar cada fuerte:\n");
                            Integer iteraciones = preguntarCantidad("Introduzca el número de iteraciones (umbrales) utilizado para generar cada débil:\n");
                            String filename = preguntarFichero("Introduzca el nombre de fichero donde se van a guardar los clasificadores fuertes:\n");
                            entrenarAdaboost(filename, trainingPercent, iteraciones, clasificadoresDebiles);
                            break;
                        case 2:
                            Integer testPercent = preguntarCantidad("Introduzca el porcentaje de imágenes a usar para pasar el test (0-100):\n");
                            String fichero = preguntarFichero("Introduzca el nombre de fichero de donde se van a leer los clasificadores fuertes:\n");
                            try{
                                new FileReader(fichero+".txt");
                                pasarTests(fichero, testPercent);
                            } catch(FileNotFoundException e){
                                JOptionPane.showMessageDialog(null, "Error, fichero no encontrado");
                            }
                            break;
                        case 3:
                            System.exit(0);
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "Error, no es una opción válida");
                            break;
                    }
                } catch(NumberFormatException e){
                    JOptionPane.showMessageDialog(null, "Error, introduzca una opción numérica");
                    opcion = -1;
                }
            }
        }while(opcion != 3);
    }
    
    private static Integer preguntarCantidad(String mensaje){
        Integer cantidad;
        do{
            String respuesta = JOptionPane.showInputDialog(mensaje);
            if(respuesta == null || respuesta.contentEquals("")){
                cantidad = -1;
                JOptionPane.showMessageDialog(null, "Error, introduzca un valor numérico");
            }else{
                try{
                    cantidad = Integer.parseInt(respuesta);
                    if(cantidad <= 0){
                        JOptionPane.showMessageDialog(null, "Error, introduzca un valor positivo");
                    }
                } catch(NumberFormatException e){
                    JOptionPane.showMessageDialog(null, "Error, introduzca un valor numérico");
                    cantidad = -1;
                }
            }
        }while(cantidad <= 0);
        return cantidad;
    }
    
    private static String preguntarFichero(String mensaje){
        String fichero;
        String respuesta = JOptionPane.showInputDialog(mensaje);
        if(respuesta == null){
            fichero = "";
            JOptionPane.showMessageDialog(null, "Error, introduzca una cadena de texto");
        }else if(respuesta.contentEquals("")){
            fichero = "ClasificadoresFuertes"; //por defecto
        }else{
            fichero = respuesta;
        }
        return fichero;
    }
    
    private static void entrenarAdaboost(String filename, Integer trainingPercent, Integer iteraciones, Integer clasificadoresDebiles){
        MNISTLoader ml = new MNISTLoader();
        ml.loadDBFromPath("./mnist_1000");
        
        ArrayList<ArrayList<Imagen>> imagenes = ml.getTrainingImages(trainingPercent);
        Integer tamImages = 0;
        tamImages = imagenes.stream().map((arraylist) -> arraylist.size()).reduce(tamImages, Integer::sum);
        System.out.println("Total imágenes entrenamiento: "+tamImages);
        System.out.println("###### Entrenamiento ######");
        
        JSONObject json = new JSONObject();
        String aciertos = "###### Entrenamiento ######\n";
        for(int numeroEntrenando=0; numeroEntrenando<10; numeroEntrenando++){
            ArrayList<ArrayList<Boolean>> esperados = getEsperados(numeroEntrenando, imagenes);
            ArrayList<ArrayList<Float>> pesos = getPesos(imagenes, tamImages);
            ClasificadorFuerte.set(esperados, pesos, tamImages, iteraciones, clasificadoresDebiles);
            
            ArrayList<ClasificadorDebil> clasificadorFuerte = ClasificadorFuerte.adaBoost(imagenes);
            json.put(numeroEntrenando, getJSONFuerte(clasificadorFuerte));
            ArrayList<ArrayList<Boolean>> resultadosFuerte = ClasificadorFuerte.aplicarClasificadorFuerte(clasificadorFuerte, imagenes);
            Integer aciertosFuerte = ClasificadorFuerte.obtenerAciertosFuerte(resultadosFuerte);
            aciertos += ("Acierto del num " +numeroEntrenando+ ": " + aciertosFuerte*100/tamImages + "%\n");
            System.out.println("Acierto del num " +numeroEntrenando+ ": " + aciertosFuerte*100/tamImages + "%");
        }
        System.out.println("###########################");
        aciertos += "###########################";
        JOptionPane.showMessageDialog(null, aciertos);
        escribirFichero(filename, json);
        //System.out.println(json);
    }
    
    private static void pasarTests(String filename, Integer testPercent){
        MNISTLoader ml = new MNISTLoader();
        ml.loadDBFromPath("./mnist_1000");
        
        ArrayList<ArrayList<Imagen>> imagenes = ml.getTestImages(testPercent);
        Integer tamImages = 0;
        tamImages = imagenes.stream().map((arraylist) -> arraylist.size()).reduce(tamImages, Integer::sum);
        System.out.println("Total imágenes test: "+tamImages);
        System.out.println("###### Test ######");
        String aciertos = "###### Test ######\n";
        ArrayList<ArrayList<ClasificadorDebil>> clasificadoresFuertes = leerFichero(filename);
        for(int numeroEntrenando=0; numeroEntrenando<10; numeroEntrenando++){
            ArrayList<ArrayList<Boolean>> esperados = getEsperados(numeroEntrenando, imagenes);
            ClasificadorFuerte.set(esperados, null, tamImages, -1, -1);
            
            ArrayList<ClasificadorDebil> clasificadorFuerte = clasificadoresFuertes.get(numeroEntrenando);
            ArrayList<ArrayList<Boolean>> resultadosFuerte = ClasificadorFuerte.aplicarClasificadorFuerte(clasificadorFuerte, imagenes);
            Integer aciertosFuerte = ClasificadorFuerte.obtenerAciertosFuerte(resultadosFuerte);
            aciertos += ("Acierto del num " +numeroEntrenando+ ": " + aciertosFuerte*100/tamImages + "%\n");
            System.out.println("Acierto del num " +numeroEntrenando+ ": " + aciertosFuerte*100/tamImages + "%");
        }
        System.out.println("##################");
        aciertos += "##################";
        JOptionPane.showMessageDialog(null, aciertos);
    }
    
    private static ArrayList<ArrayList<Float>> getPesos(ArrayList<ArrayList<Imagen>> imagenes, Integer tamImages){
        ArrayList<ArrayList<Float>> pesos = new ArrayList<>();
        for(int i=0; i<10; i++){
            ArrayList<Float> peso = new ArrayList<>();
            for(int j=0; j<imagenes.get(i).size(); j++){
                peso.add((float) 1/tamImages);
            }
            pesos.add(peso);
        }
        return pesos;
    }
    
    private static ArrayList<ArrayList<Boolean>> getEsperados(Integer numeroEntrenando, ArrayList<ArrayList<Imagen>> imagenes){
        ArrayList<ArrayList<Boolean>> esperados = new ArrayList<>();
        for(int i=0; i<10; i++){
            ArrayList<Boolean> esperado = new ArrayList<>();
            for(int j=0; j<imagenes.get(i).size(); j++){
                if(i == numeroEntrenando){
                    esperado.add(true);
                } else{
                    esperado.add(false);
                }
            }
            esperados.add(esperado);
        }
        return esperados;
    }

    private static JSONArray getJSONFuerte(ArrayList<ClasificadorDebil> casificadorFuerte){
        JSONArray lista = new JSONArray();
        for (ClasificadorDebil debil : casificadorFuerte) {
            JSONObject jsonDebil = new JSONObject();
            jsonDebil.put("pixel", debil.getPixel());
            jsonDebil.put("umbral", debil.getUmbral());
            jsonDebil.put("direccion", debil.getDireccion());
            lista.add(jsonDebil);
        }
        return lista;
    }
    
    private static void escribirFichero(String filename, JSONObject json){
        try (FileWriter file = new FileWriter(filename+".txt")) {
            file.write(json.toJSONString());
            file.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static ArrayList<ArrayList<ClasificadorDebil>> leerFichero(String filename){
        ArrayList<ArrayList<ClasificadorDebil>> clasificadoresFuertes = new ArrayList<>();
        try{
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(filename+".txt"));
            JSONObject jsonObject = (JSONObject) obj;
            //System.out.println(jsonObject.toString());
            for(Integer numeroEntrenando=0; numeroEntrenando<10; numeroEntrenando++){
                ArrayList<ClasificadorDebil> clasificadorFuerte = new ArrayList<>();
                JSONArray fuerteJSON = (JSONArray) jsonObject.get(numeroEntrenando.toString());
                Iterator<JSONObject> iterator = fuerteJSON.iterator();
                while (iterator.hasNext()) {
                    JSONObject debilJSON = iterator.next();
                    Integer pixel = Integer.parseInt(debilJSON.get("pixel").toString());
                    Integer umbral = Integer.parseInt(debilJSON.get("umbral").toString());
                    Integer direccion = Integer.parseInt(debilJSON.get("direccion").toString());
                    ClasificadorDebil debil = new ClasificadorDebil(pixel, umbral, direccion);
                    clasificadorFuerte.add(debil);
                }
                clasificadoresFuertes.add(clasificadorFuerte);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return clasificadoresFuertes;
    }
}