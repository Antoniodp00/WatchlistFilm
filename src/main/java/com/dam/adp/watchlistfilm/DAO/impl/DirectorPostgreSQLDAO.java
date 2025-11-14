package com.dam.adp.watchlistfilm.DAO.impl;

import com.dam.adp.watchlistfilm.DAO.IDirectorDAO;
import com.dam.adp.watchlistfilm.model.Director;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DirectorPostgreSQLDAO implements IDirectorDAO {

    private Connection connection;

    private static final String SELECT_BY_ID =
            "SELECT director_id, nombre_director FROM Director WHERE director_id = ?";
    private static final String SELECT_ID_BY_NOMBRE =
            "SELECT director_id FROM Director WHERE nombre_director = ?";
    private static final String INSERT =
            "INSERT INTO Director (nombre_director) VALUES (?) RETURNING director_id";
    private static final String SELECT_BY_PELICULA_ID =
            "SELECT d.director_id, d.nombre_director FROM Director d " +
                    "JOIN Pelicula_Director pd ON d.director_id = pd.director_id WHERE pd.pelicula_id = ?";

    @Override
    public Director getById(int id) throws SQLException {
        Director director = null;
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    director = new Director(rs.getInt("director_id"), rs.getString("nombre_director"));
                }
            }
        }
        return director;
    }

    @Override
    public int getIdByNombre(String nombre) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SELECT_ID_BY_NOMBRE)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("director_id");
                }
            }
        }
        return -1;
    }

    @Override
    public int insertarDirector(Director director) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(INSERT, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, director.getNombre_director());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                System.err.println("Advertencia: Director duplicado encontrado. Buscado id...");
                return getIdByNombre(director.getNombre_director());
            } else {
                throw e;
            }
        }
        return -1;
    }

    @Override
    public List<Director> getDirectoresPorPeliculaId(int peliculaId) throws SQLException {
        List<Director> directores = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_PELICULA_ID)) {
            ps.setInt(1, peliculaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    directores.add(new Director(
                            rs.getInt("director_id"),
                            rs.getString("nombre_director")));
                }
            }
        }
        return directores;
    }
}
