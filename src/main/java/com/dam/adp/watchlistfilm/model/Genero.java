package com.dam.adp.watchlistfilm.model;

public class Genero {

    private int genero_id;
    private String nombre_genero;

    // Constructor vacío
    public Genero() {
    }


    public Genero(String nombre_genero) {
        this.nombre_genero = nombre_genero;
    }


    public Genero(int genero_id, String nombre_genero) {
        this.genero_id = genero_id;
        this.nombre_genero = nombre_genero;
    }



    public int getGenero_id() {
        return genero_id;
    }

    public void setGenero_id(int genero_id) {
        this.genero_id = genero_id;
    }

    public String getNombre_genero() {
        return nombre_genero;
    }

    public void setNombre_genero(String nombre_genero) {
        this.nombre_genero = nombre_genero;
    }

    @Override
    public String toString() {
        return "Genero{" +
                "genero_id=" + genero_id +
                ", nombre_genero='" + nombre_genero + '\'' +
                '}';
    }
}