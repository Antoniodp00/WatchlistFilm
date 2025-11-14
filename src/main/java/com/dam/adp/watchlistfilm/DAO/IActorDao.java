package com.dam.adp.watchlistfilm.DAO;

import com.dam.adp.watchlistfilm.model.Actor;

import java.sql.SQLException;
import java.util.List;

public interface IActorDao {
    // Métodos de consulta general
    Actor getById(int id) throws SQLException;
    int getIdByNombre(String nombre) throws SQLException;

    // Método CRUD (necesario para la Transacción CSV)
    int insertarActor(Actor actor) throws SQLException;

    // Método relacional (usado por PeliculaDAO para hidratar el objeto Pelicula)
    List<Actor> getActoresPorPeliculaId(int peliculaId) throws SQLException;
}
