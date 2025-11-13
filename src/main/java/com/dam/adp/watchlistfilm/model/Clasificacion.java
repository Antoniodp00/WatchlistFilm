package com.dam.adp.watchlistfilm.model;

public class Clasificacion {

    private int clasificacion_id;
    private String codigo_clasificacion;

    // Constructor vacío
    public Clasificacion() {
    }

    // Constructor para insertar (sin ID)
    public Clasificacion(String codigo_clasificacion) {
        this.codigo_clasificacion = codigo_clasificacion;
    }

    // Constructor completo (para leer)
    public Clasificacion(int clasificacion_id, String codigo_clasificacion) {
        this.clasificacion_id = clasificacion_id;
        this.codigo_clasificacion = codigo_clasificacion;
    }

    // --- Getters y Setters ---

    public int getClasificacion_id() {
        return clasificacion_id;
    }

    public void setClasificacion_id(int clasificacion_id) {
        this.clasificacion_id = clasificacion_id;
    }

    public String getCodigo_clasificacion() {
        return codigo_clasificacion;
    }

    public void setCodigo_clasificacion(String codigo_clasificacion) {
        this.codigo_clasificacion = codigo_clasificacion;
    }

    @Override
    public String toString() {
        return "Clasificacion{" +
                "clasificacion_id=" + clasificacion_id +
                ", codigo_clasificacion='" + codigo_clasificacion + '\'' +
                '}';
    }
}