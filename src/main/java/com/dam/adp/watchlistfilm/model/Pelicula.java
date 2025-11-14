package com.dam.adp.watchlistfilm.model;

import java.util.List;

/**
 * POJO Relacional. Representa la Pelicula y sus objetos relacionados.
 */
public class Pelicula {

    private int pelicula_id;
    private String titulo;
    private int ano_lanzamiento;
    private double imdb_rating;
    private int duracion_min;


    // 1:N -> Relacion con Clasificacion
    private Clasificacion clasificacion;

    // N:M -> Listas para almacenar los objetos relacionados
    private List<Director> directores;
    private List<Actor> actores;
    private List<Genero> generos;


    public Pelicula() {
    }

    public Pelicula(int pelicula_id, String titulo, int ano_lanzamiento, double imdb_rating,
                    int duracion_min, Clasificacion clasificacion, List<Director> directores,
                    List<Actor> actores, List<Genero> generos) {
        this.pelicula_id = pelicula_id;
        this.titulo = titulo;
        this.ano_lanzamiento = ano_lanzamiento;
        this.imdb_rating = imdb_rating;
        this.duracion_min = duracion_min;
        this.clasificacion = clasificacion;
        this.directores = directores;
        this.actores = actores;
        this.generos = generos;
    }


    public int getPelicula_id() { return pelicula_id; }
    public void setPelicula_id(int pelicula_id) { this.pelicula_id = pelicula_id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public int getAno_lanzamiento() { return ano_lanzamiento; }
    public void setAno_lanzamiento(int ano_lanzamiento) { this.ano_lanzamiento = ano_lanzamiento; }
    public double getImdb_rating() { return imdb_rating; }
    public void setImdb_rating(double imdb_rating) { this.imdb_rating = imdb_rating; }
    public int getDuracion_min() { return duracion_min; }
    public void setDuracion_min(int duracion_min) { this.duracion_min = duracion_min; }



    public Clasificacion getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(Clasificacion clasificacion) {
        this.clasificacion = clasificacion;
    }

    public List<Director> getDirectores() {
        return directores;
    }

    public void setDirectores(List<Director> directores) {
        this.directores = directores;
    }

    public List<Actor> getActores() {
        return actores;
    }

    public void setActores(List<Actor> actores) {
        this.actores = actores;
    }

    public List<Genero> getGeneros() {
        return generos;
    }

    public void setGeneros(List<Genero> generos) {
        this.generos = generos;
    }

    @Override
    public String toString() {
        return "Pelicula{" +
                "pelicula_id=" + pelicula_id +
                ", titulo='" + titulo + '\'' +
                ", ano_lanzamiento=" + ano_lanzamiento +
                ", imdb_rating=" + imdb_rating +
                ", duracion_min=" + duracion_min +
                ", clasificacion=" + clasificacion +
                ", directores=" + directores +
                ", actores=" + actores +
                ", generos=" + generos +
                '}';
    }
}