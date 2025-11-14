package com.dam.adp.watchlistfilm.model;


public class Actor {

    private int actor_id;
    private String nombre_actor;


    public Actor() {
    }


    public Actor(String nombre_actor) {
        this.nombre_actor = nombre_actor;
    }

    // Constructor completo (para leer)
    public Actor(int actor_id, String nombre_actor) {
        this.actor_id = actor_id;
        this.nombre_actor = nombre_actor;
    }



    public int getActor_id() {
        return actor_id;
    }

    public void setActor_id(int actor_id) {
        this.actor_id = actor_id;
    }

    public String getNombre_actor() {
        return nombre_actor;
    }

    public void setNombre_actor(String nombre_actor) {
        this.nombre_actor = nombre_actor;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "actor_id=" + actor_id +
                ", nombre_actor='" + nombre_actor + '\'' +
                '}';
    }
}