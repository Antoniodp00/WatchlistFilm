package com.dam.adp.watchlistfilm.DAO.impl;

import com.dam.adp.watchlistfilm.DAO.IClasificacionDAO;
import com.dam.adp.watchlistfilm.db.ConexionPostgreSQL;
import com.dam.adp.watchlistfilm.model.Clasificacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ClasificacionPostgreSQLDAO implements IClasificacionDAO {

    private Connection connection;

    // CONSULTAS SQL
    private static final String SELECT_BY_ID =
            "SELECT clasificacion_id, codigo_clasificacion FROM Clasificacion WHERE clasificacion_id = ?";
    private static final String SELECT_BY_CODIGO =
            "SELECT clasificacion_id, codigo_clasificacion FROM Clasificacion WHERE codigo_clasificacion = ?";
    private static final String INSERT =
            "INSERT INTO Clasificacion (codigo_clasificacion) VALUES (?) RETURNING clasificacion_id";

    public ClasificacionPostgreSQLDAO() {
        this.connection = ConexionPostgreSQL.getInstance().getConnection();
    }

    @Override
    public Clasificacion getById(int id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Clasificacion(rs.getInt("clasificacion_id"), rs.getString("codigo_clasificacion"));
                }
            }
        }
        return null;
    }

    @Override
    public Clasificacion getByCodigo(String codigo) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_CODIGO)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Devuelve el objeto Clasificacion completo si existe
                    return new Clasificacion(rs.getInt("clasificacion_id"), rs.getString("codigo_clasificacion"));
                }
            }
        }
        return null; // Devuelve null si no existe
    }

    @Override
    public int insertarClasificacion(Clasificacion clasificacion) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, clasificacion.getCodigo_clasificacion());

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
            // Error 23505: Violación de clave única (la clasificación ya existe)
            if (e.getSQLState().equals("23505")) {
                System.err.println("Advertencia: Clasificación duplicada encontrada. Buscando ID...");

                // Buscamos y devolvemos el ID existente
                Clasificacion existente = getByCodigo(clasificacion.getCodigo_clasificacion());
                if (existente != null) {
                    return existente.getClasificacion_id();
                }
            }
            // Si es cualquier otro error, o no se encontró el existente, lo lanzamos.
            throw e;
        }
        return -1; // Fallo en la inserción
    }
}