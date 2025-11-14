package com.dam.adp.watchlistfilm.DAO.impl;

import com.dam.adp.watchlistfilm.DAO.IPeliculaDAO;
import com.dam.adp.watchlistfilm.model.Pelicula;

import java.sql.SQLException;
import java.util.List;

public class PeliculaPostgreSQLImpl implements IPeliculaDAO {
    @Override
    public Pelicula getPeliculaCompletaPorId(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Pelicula> bucarPeliculaPorTitulo(String titulo) throws SQLException {
        return List.of();
    }

    @Override
    public List<Pelicula> bucarPeliculaPorGenero(String genero) throws SQLException {
        return List.of();
    }

    @Override
    public List<Pelicula> bucarPeliculaPorDirector(String director) throws SQLException {
        return List.of();
    }

    @Override
    public List<Pelicula> bucarPeliculaPorActor(String actor) throws SQLException {
        return List.of();
    }

    @Override
    public List<Pelicula> bucarPeliculaPorClasificacion(String clasificacion) throws SQLException {
        return List.of();
    }

    @Override
    public List<Pelicula> getAllPeliculas() throws SQLException {
        return List.of();
    }
}
