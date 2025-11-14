package com.dam.adp.watchlistfilm.DAO;

import com.dam.adp.watchlistfilm.model.Pelicula;

import java.sql.SQLException;
import java.util.List;

public interface IPeliculaDAO {

    Pelicula getPeliculaCompletaPorId(int id) throws SQLException;

    List<Pelicula> bucarPeliculaPorTitulo(String titulo) throws SQLException;

    List<Pelicula> bucarPeliculaPorGenero(String genero) throws SQLException;

    List<Pelicula> bucarPeliculaPorDirector(String director) throws SQLException;

    List<Pelicula> bucarPeliculaPorActor(String actor) throws SQLException;

    List<Pelicula> bucarPeliculaPorClasificacion(String clasificacion) throws SQLException;

    List<Pelicula> getAllPeliculas() throws SQLException;
}
