/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica2;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author larry
 */
public class MNISTLoader {

    private ArrayList<ArrayList<Imagen>> mnistImageDB;
    
    void loadDBFromPath(String path){
        int imagesCount=0;
        //Una arrayList por dígito almacenará las imágenes
        mnistImageDB = new ArrayList<>();
        
        //Creo un array list de imagenes para cada dígito y cargo cada una
        //de las imágenes disponibles por dígito
        for (int i=0;i<10; i++){
            mnistImageDB.add(new ArrayList<>());
            System.out.println("Loaded digit "+i);
            File[] files = new File(path,"d"+i).listFiles();
            for (File file : files) {
                if (file.isFile()) {
                   mnistImageDB.get(i).add(new Imagen(file.getAbsoluteFile()));
                   imagesCount++;
                }
            }
        }        
        System.out.println("Loaded "+ imagesCount + " images...");
    }
    
    ArrayList<Imagen> getImageDatabaseForDigit(int digit){
        System.out.println("Digito " + digit +": " + mnistImageDB.get(digit).size());
        return mnistImageDB.get(digit);
    }
    
    ArrayList<ArrayList<Imagen>> getTrainingImages(Integer percent){
        ArrayList<ArrayList<Imagen>> training = new ArrayList();
        for(int i=0; i<mnistImageDB.size();i++){
            Integer max = Math.round(mnistImageDB.get(i).size()*percent/100);
            ArrayList<Imagen> imagenes = new ArrayList<>();
            for(int j=0; j<max; j++){
                imagenes.add(mnistImageDB.get(i).get(j));
            }
            training.add(imagenes);
        }
        System.out.println("Training: " + training.get(0).size());
        return training;
    }
    
    ArrayList<ArrayList<Imagen>> getTestImages(Integer percent){
        ArrayList<ArrayList<Imagen>> test = new ArrayList();
        for(int i=0; i<mnistImageDB.size();i++){
            Integer max = Math.round(mnistImageDB.get(i).size()*(100-percent)/100);
            ArrayList<Imagen> imagenes = new ArrayList<>();
            for(int j=mnistImageDB.get(i).size()-1; j>=max; j--){
                imagenes.add(mnistImageDB.get(i).get(j));
            }
            test.add(imagenes);
        }
        System.out.println("Test: " + test.get(0).size());
        return test;
    }  
}
