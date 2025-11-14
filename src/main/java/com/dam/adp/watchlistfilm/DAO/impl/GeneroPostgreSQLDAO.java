package com.dam.adp.watchlistfilm.DAO.impl;

import com.dam.adp.watchlistfilm.DAO.IGeneroDAO;
import com.dam.adp.watchlistfilm.db.ConexionPostgreSQL;
import com.dam.adp.watchlistfilm.model.Genero;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GeneroPostgreSQLDAO implements IGeneroDAO {

    private Connection connection;


    private static final String SELECT_BY_ID =
            "SELECT genero_id, nombre_genero FROM Genero WHERE genero_id = ?";
    private static final String SELECT_ID_BY_NOMBRE =
            "SELECT genero_id FROM Genero WHERE nombre_genero = ?";
    private static final String INSERT =
            "INSERT INTO Genero (nombre_genero) VALUES (?) RETURNING genero_id";
    private static final String SELECT_BY_PELICULA_ID =
            "SELECT g.genero_id, g.nombre_genero FROM Genero g " +
                    "JOIN Pelicula_Genero pg ON g.genero_id = pg.genero_id WHERE pg.pelicula_id = ?";

    public GeneroPostgreSQLDAO() {
        this.connection = ConexionPostgreSQL.getInstance().getConnection();
    }

    @Override
    public Genero getById(int id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Genero(rs.getInt("genero_id"), rs.getString("nombre_genero"));
                }
            }
        }
        return null;
    }

    @Override
    public int getIdByNombre(String nombre) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SELECT_ID_BY_NOMBRE)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("genero_id"); // Devuelve el ID si existe
                }
            }
        }
        return -1; // Devuelve -1 si el género no existe
    }

    @Override
    public int insertarGenero(Genero genero) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, genero.getNombre_genero());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                // Éxito: Devolvemos el ID generado por la BBDD
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            // Error 23505: Violación de clave única (el género ya existe)
            if (e.getSQLState().equals("23505")) {
                System.err.println("Advertencia: Género duplicado encontrado. Buscando ID...");
                // Devolvemos el ID existente para que la Transacción pueda usarlo
                return getIdByNombre(genero.getNombre_genero());
            }

            throw e;
        }
        return -1; // Fallo en la inserción
    }

    @Override
    public List<Genero> getGenerosPorPeliculaId(int peliculaId) throws SQLException {
        List<Genero> generos = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_PELICULA_ID)) {
            ps.setInt(1, peliculaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    generos.add(new Genero(
                            rs.getInt("genero_id"),
                            rs.getString("nombre_genero")
                    ));
                }
            }
        }
        return generos;
    }
}