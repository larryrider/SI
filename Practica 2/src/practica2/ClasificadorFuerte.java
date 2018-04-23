/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica2;

import java.util.ArrayList;

/**
 *
 * @author larry
 */
public class ClasificadorFuerte {
    
    private static ArrayList<ArrayList<Boolean>> esperados = new ArrayList<>();
    private static ArrayList<ArrayList<Float>> pesos = new ArrayList<>();
    private static Integer tamImages;
    private static Integer iteraciones, clasificadoresDebiles;
    
    public static void set(ArrayList<ArrayList<Boolean>> esperados, ArrayList<ArrayList<Float>> pesos, Integer tamImages, Integer iteraciones, Integer debiles){
        ClasificadorFuerte.esperados = esperados;
        ClasificadorFuerte.pesos = pesos;
        ClasificadorFuerte.tamImages = tamImages;
        ClasificadorFuerte.iteraciones = iteraciones;
        ClasificadorFuerte.clasificadoresDebiles = debiles;
    }

    public static ArrayList<ClasificadorDebil> adaBoost(ArrayList<ArrayList<Imagen>> imagenes) {
        //Integer iteraciones = 1, clasificadoresDebiles = 2;
        ArrayList<ClasificadorDebil> mejorDebiles = new ArrayList<>();
        for (int i = 0; i < clasificadoresDebiles; i++) {
            ClasificadorDebil mejorDebil = null;
            for (int j = 0; j < iteraciones; j++) {
                ClasificadorDebil clasificadorDebil = ClasificadorDebil.generarClasificadorAzar();
                clasificadorDebil.aplicarClasificadorDebil(imagenes);
                clasificadorDebil.obtenerErrorClasificador(esperados, pesos);
                actualizarPesos(clasificadorDebil.getConfianza(), clasificadorDebil.getResultados());
                if (mejorDebil == null && clasificadorDebil.getError() <= 0.5) {
                    mejorDebil = clasificadorDebil;
                } else {
                    if (mejorDebil != null && clasificadorDebil.getError() <= 0.5 && clasificadorDebil.getError() < mejorDebil.getError()) {
                        mejorDebil = clasificadorDebil;
                    }
                }
            }
            if (mejorDebil == null) {
                i--;
            } else {
                mejorDebiles.add(mejorDebil);
                if(obtenerAciertosFuerte(aplicarClasificadorFuerte(mejorDebiles, imagenes)).equals(tamImages)){
                    break;
                }
            }
        }
        return mejorDebiles;
    }

    public static void actualizarPesos(Float confianza, ArrayList<ArrayList<Boolean>> resultados) {
        Float suma = 0f;
        for (int i = 0; i < pesos.size(); i++) {
            for (int j = 0; j < pesos.get(i).size(); j++) {
                Float peso = pesos.get(i).get(j);
                Boolean esperado = esperados.get(i).get(j);
                Boolean real = resultados.get(i).get(j);
                Integer y = esperado ? 1 : -1, h = real ? 1 : -1;
                pesos.get(i).set(j, (float) (peso * Math.pow(Math.E, (double) -1 * confianza * y * h)));
                suma += pesos.get(i).get(j);
            }
        }
        for (int i = 0; i < pesos.size(); i++) {
            for (int j = 0; j < pesos.get(i).size(); j++) {
                Float peso = pesos.get(i).get(j);
                pesos.get(i).set(j, peso / suma);
            }
        }
    }

    public static ArrayList<ArrayList<Boolean>> aplicarClasificadorFuerte(ArrayList<ClasificadorDebil> mejorDebiles, ArrayList<ArrayList<Imagen>> imagenes){
        ArrayList<ArrayList<Boolean>> decision = new ArrayList<>();
        for(int i=0; i<imagenes.size(); i++){
            ArrayList<Boolean> decisiones = new ArrayList<>();
            for(int j=0; j<imagenes.get(i).size(); j++){  //para cada imagen se mira lo que dicen los mejoresDebiles
                Float signo = 0f;
                for(int k=0; k<mejorDebiles.size(); k++){
                    Boolean resultado = mejorDebiles.get(k).aplicarClasificadorDebil(imagenes.get(i).get(j));
                    signo += mejorDebiles.get(k).getConfianza() * (resultado ? 1:-1);
                }
                if(signo >= 0){
                    decisiones.add(true);
                } else{
                    decisiones.add(false);
                }
            }
            decision.add(decisiones);
        }
        return decision;
    }
    
    public static Integer obtenerAciertosFuerte(ArrayList<ArrayList<Boolean>> resultados){
        Integer acierto = 0;
        for(int i=0; i<esperados.size(); i++){
            for(int j=0;j<esperados.get(i).size();j++){
                if(esperados.get(i).get(j).equals(resultados.get(i).get(j))){
                    acierto++;
                }
            }
        }
        return acierto;
    }

}
