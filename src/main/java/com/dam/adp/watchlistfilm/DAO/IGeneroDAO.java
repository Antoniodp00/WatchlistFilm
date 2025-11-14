package com.dam.adp.watchlistfilm.DAO;

import com.dam.adp.watchlistfilm.model.Genero;

import java.sql.SQLException;
import java.util.List;

public interface IGeneroDAO {
    // Métodos de consulta general
    Genero getById(int id) throws SQLException;
    int getIdByNombre(String nombre) throws SQLException;

    // Método CRUD (necesario para la Transacción CSV)
    int insertarGenero(Genero genero) throws SQLException;

    // Método relacional (usado por PeliculaDAO)
    List<Genero> getGenerosPorPeliculaId(int peliculaId) throws SQLException;
}
