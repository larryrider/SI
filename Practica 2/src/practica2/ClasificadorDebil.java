/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica2;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author larry
 */
public class ClasificadorDebil {
    
    private final Integer pixel; //0 y 784
    private final Integer umbral; //0 y 255
    private final Integer direccion; //1 y -1
    private Float error;
    private ArrayList<ArrayList<Boolean>> resultados;

    public ClasificadorDebil(Integer pixel, Integer umbral, Integer direccion) {
        this.pixel = pixel;
        this.umbral = umbral;
        this.direccion = direccion;
        this.error = 0f;
        this.resultados = new ArrayList<>();
    }

    public static ClasificadorDebil generarClasificadorAzar(){
        Random rand = new Random(System.currentTimeMillis());
        
        Integer pixelAux = (rand.nextInt((783 - 0) + 1) + 0);
        Integer umbralAux = (rand.nextInt((255 - 0) + 1) + 0);
        Integer direccionAux = (rand.nextInt((1 - 0) + 1) + 0);
        if(direccionAux == 0){
            direccionAux = -1;
        }
        return new ClasificadorDebil(pixelAux, umbralAux, direccionAux);
    }
    
    public void aplicarClasificadorDebil(ArrayList<ArrayList<Imagen>> imagenes){
        for(int i=0; i<imagenes.size(); i++){
            ArrayList<Boolean> decision = new ArrayList<>();
            for (int j=0; j<imagenes.get(i).size(); j++){
                Imagen imagen = imagenes.get(i).get(j);
                byte pixeles[] = imagen.getImageData();
                Integer pixelImagen = Byte.toUnsignedInt(pixeles[getPixel()]);
                //System.out.println("pixel: " + getPixel() + " umbral: " + pixelImagen);
                if(getDireccion() == 1){
                    if(pixelImagen >= getUmbral()){
                        decision.add(true);
                    } else{
                        decision.add(false);
                    }
                } else{
                    if(pixelImagen <= getUmbral()){
                        decision.add(true);
                    } else{
                        decision.add(false);
                    }
                }
            }
            resultados.add(decision);
        }
    }
    
    public Boolean aplicarClasificadorDebil(Imagen imagen){
        Boolean decision;
        byte pixeles[] = imagen.getImageData();
        Integer pixelImagen = Byte.toUnsignedInt(pixeles[getPixel()]);
        //System.out.println("pixel: " + getPixel() + " umbral: " + pixelImagen);
        if(getDireccion() == 1){
            decision = pixelImagen >= getUmbral();
        } else{
            decision = pixelImagen <= getUmbral();
        }
        return decision;
    }
    
    public void obtenerErrorClasificador(ArrayList<ArrayList<Boolean>> esperados, ArrayList<ArrayList<Float>> pesos){
        error = 0f;
        for(int i=0; i<esperados.size(); i++){
            //System.out.println("real: " + real.get(i) + " esperado: " + esperado.get(i));
            for(int j=0;j<esperados.get(i).size();j++){
                //System.out.println("real: " + resultados.get(i).get(j) + " esperado: " + esperados.get(i).get(j));
                if(!esperados.get(i).get(j).equals(resultados.get(i).get(j))){
                    error+=pesos.get(i).get(j);
                }
            }
        }
        //System.out.println("Tamaño real: " + real.size() + " Tamaño esperado: " + esperado.size());
        //System.out.println("error: " + error);
    }

    public Float getError() {
        return error;
    }

    public ArrayList<ArrayList<Boolean>> getResultados() {
        return resultados;
    }
    
    public Float getConfianza(){
        return (float) (0.5*Math.log((1-error)/error));
    }
    
    public Integer getPixel() {
        return pixel;
    }

    public Integer getUmbral() {
        return umbral;
    }

    public Integer getDireccion() {
        return direccion;
    }
}
