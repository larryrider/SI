/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica1;

import java.util.ArrayList;

/**
 *
 * @author larry
 */
public class Nodo implements Comparable<Nodo>{
    
    private final int x,y;
    private Nodo nodoPadre;
    private static int destino, tamaño, mundo[][];

    
    public static void inicializarNodos(int destinoAux, int tamañoAux, int mundoAux[][]){
        destino = destinoAux;
        tamaño = tamañoAux;
        mundo = mundoAux;
    }
    
    public Nodo(int x, int y, Nodo nodoPadre) {
        this.x = x;
        this.y = y;
        this.nodoPadre = nodoPadre;
    }
    
    public Integer getFuncionG(){
        Integer g = 0;
        if(getNodoPadre() != null){
            g = 1 + getNodoPadre().getFuncionG();
        }
        return g;
    }
    
    public Integer getFuncionH(){
        return (int) Math.abs(destino-x) + Math.abs((tamaño-1)-y); //distancia manhattan
        //return (int) Math.round(Math.sqrt(Math.pow((destino-x), 2) + Math.pow(((tamaño-1)-y), 2))); //euclidea
    }

    public Integer getFuncionF(){
        return getFuncionG() + getFuncionH();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public Nodo getNodoPadre() {
        return nodoPadre;
    }
    
    public void setNodoPadre(Nodo nodoPadre){
        this.nodoPadre = nodoPadre;
    }

    public boolean esIgual(Nodo nodo) {
        return (x == nodo.getX() && y == nodo.getY());
    }

    public Boolean esMeta(){
        return (x == destino && y == tamaño-2);
    }

    public ArrayList<Nodo> getHijosNoAñadidosEn(ArrayList<Nodo> lista){ //Que no esten ya en la lista
        ArrayList<Nodo> hijos = new ArrayList<Nodo>();
        if (mundo[getX()][getY() + 1] == 0){ //derecha
            hijos.add(new Nodo(getX(), getY() + 1, this));
        }
        if (mundo[getX()][getY() - 1] == 0){ //izquierda
            hijos.add(new Nodo(getX(), getY() - 1, this));
        }
        if (mundo[getX() + 1][getY()] == 0){ //abajo
            hijos.add(new Nodo(getX() + 1, getY(), this));
        }
        if (mundo[getX() - 1][getY()] == 0){ //arriba
            hijos.add(new Nodo(getX() - 1, getY(), this));
        }
        for(int i = 0; i < hijos.size(); i++){
            if(hijos.get(i).estaEn(lista)){
                hijos.remove(i);
                i--;
            }
        }
        return hijos;
    }
    
    public Boolean estaEn(ArrayList<Nodo> lista){
        for(Nodo n: lista){
            if(this.esIgual(n)){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int compareTo(Nodo nodo) {
        if(getFuncionF().compareTo(nodo.getFuncionF()) == 0){
            if(getFuncionH().compareTo(nodo.getFuncionH()) == 0){
                return getFuncionG().compareTo(nodo.getFuncionG());
            } else{
                return getFuncionH().compareTo(nodo.getFuncionH());
            }
        } else{
            return getFuncionF().compareTo(nodo.getFuncionF());
        }
    }

    public Integer getPosicionEn(ArrayList<Nodo> lista){
        for(int i=0; i<lista.size(); i++){
            if(this.esIgual(lista.get(i))){
                return i;
            }
        }
        return -1;
    }
}
