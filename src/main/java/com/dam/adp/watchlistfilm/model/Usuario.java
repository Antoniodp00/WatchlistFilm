package com.dam.adp.watchlistfilm.model;

import java.util.List;

/**
 * POJO Relacional. Representa al Usuario y su lista de películas.
 */
public class Usuario {

    private int usuario_id;
    private String nombre_usuario;
    private String email;
    private String password_hash;

    private List<MiLista> miListaDePeliculas;



    public Usuario() {}
    public int getUsuario_id() { return usuario_id; }
    public void setUsuario_id(int usuario_id) { this.usuario_id = usuario_id; }
    public String getNombre_usuario() { return nombre_usuario; }
    public void setNombre_usuario(String nombre_usuario) { this.nombre_usuario = nombre_usuario; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword_hash() { return password_hash; }
    public void setPassword_hash(String password_hash) { this.password_hash = password_hash; }


    public List<MiLista> getMiListaDePeliculas() {
        return miListaDePeliculas;
    }

    public void setMiListaDePeliculas(List<MiLista> miListaDePeliculas) {
        this.miListaDePeliculas = miListaDePeliculas;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "usuario_id=" + usuario_id +
                ", nombre_usuario='" + nombre_usuario + '\'' +
                ", email='" + email + '\'' +
                ", miListaDePeliculas.size()=" + (miListaDePeliculas != null ? miListaDePeliculas.size() : 0) +
                '}';
    }
}