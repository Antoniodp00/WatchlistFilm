package com.dam.adp.watchlistfilm.model;


public class Director {

    private int director_id;
    private String nombre_director;

    public Director() {
    }


    public Director(String nombre_director) {
        this.nombre_director = nombre_director;
    }


    public Director(int director_id, String nombre_director) {
        this.director_id = director_id;
        this.nombre_director = nombre_director;
    }



    public int getDirector_id() {
        return director_id;
    }

    public void setDirector_id(int director_id) {
        this.director_id = director_id;
    }

    public String getNombre_director() {
        return nombre_director;
    }

    public void setNombre_director(String nombre_director) {
        this.nombre_director = nombre_director;
    }

    @Override
    public String toString() {
        return "Director{" +
                "director_id=" + director_id +
                ", nombre_director='" + nombre_director + '\'' +
                '}';
    }
}