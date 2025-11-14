package com.dam.adp.watchlistfilm.DAO;

import com.dam.adp.watchlistfilm.model.Director;

import java.sql.SQLException;
import java.util.List;

public interface IDirectorDAO {
    // Métodos de consulta general
    Director getById(int id) throws SQLException;
    int getIdByNombre(String nombre) throws SQLException;

    // Método CRUD (necesario para la Transacción CSV)
    int insertarDirector(Director director) throws SQLException;

    // Método relacional (usado por PeliculaDAO)
    List<Director> getDirectoresPorPeliculaId(int peliculaId) throws SQLException;
}
