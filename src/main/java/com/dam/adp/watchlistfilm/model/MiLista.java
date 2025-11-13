package com.dam.adp.watchlistfilm.model;

/**
 * POJO Relacional Bi-direccional.
 * Contiene la Pelicula y el Usuario al que pertenece.
 */
public class MiLista {

    private String estado;
    private int puntuacion;
    private String imagen_personal_path;

    // Referencia al "Padre"
    private Usuario usuario;
    // Referencia a la "Entidad Descrita"
    private Pelicula pelicula;

    // Constructor vacío
    public MiLista() {
    }

    // --- Getters y Setters ---

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public int getPuntuacion() { return puntuacion; }
    public void setPuntuacion(int puntuacion) { this.puntuacion = puntuacion; }
    public String getImagen_personal_path() { return imagen_personal_path; }
    public void setImagen_personal_path(String path) { this.imagen_personal_path = path; }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Pelicula getPelicula() {
        return pelicula;
    }

    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
    }

    @Override
    public String toString() {
        // ¡NUNCA imprimas el objeto usuario completo aquí!
        return "MiLista{" +
                "estado='" + estado + '\'' +
                ", puntuacion=" + puntuacion +
                ", pelicula_titulo=" + (pelicula != null ? pelicula.getTitulo() : "null") +
                ", usuario_id=" + (usuario != null ? usuario.getUsuario_id() : "null") +
                '}';
    }
}