package com.dam.adp.watchlistfilm.DAO.impl;

import com.dam.adp.watchlistfilm.DAO.IActorDao;
import com.dam.adp.watchlistfilm.db.ConexionPostgreSQL;
import com.dam.adp.watchlistfilm.model.Actor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ActorPostgreSQLDAO implements IActorDao {
    private Connection connection;

    private static final String SELECT_BY_ID =
            "SELECT actor_id, nombre_actor FROM Actor WHERE actor_id = ?";
    private static final String SELECT_ID_BY_NOMBRE =
            "SELECT actor_id FROM Actor WHERE nombre_actor = ?";
    private static final String INSERT =
            "INSERT INTO Actor (nombre_actor) VALUES (?) RETURNING actor_id"; // PostgreSQL necesita RETURNING
    private static final String SELECT_BY_PELICULA_ID =
            "SELECT a.actor_id, a.nombre_actor FROM Actor a " +
                    "JOIN Pelicula_Actor pa ON a.actor_id = pa.actor_id WHERE pa.pelicula_id = ?";

    public ActorPostgreSQLDAO() {
        this.connection = ConexionPostgreSQL.getInstance().getConnection();
    }


    @Override
    public Actor getById(int id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Actor(rs.getInt("actor_id"), rs.getString("nombre_actor"));
                }

            }
        }
        return null;
    }

    @Override
    public int getIdByNombre(String nombre) throws SQLException {
        int idActor = 0;
        try (PreparedStatement ps = connection.prepareStatement(SELECT_ID_BY_NOMBRE)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    idActor = rs.getInt("actor_id");
                }

            }
        }
        return idActor;
    }

    @Override
    public int insertarActor(Actor actor) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(INSERT, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, actor.getNombre_actor());

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
            if (e.getSQLState().equals("23505")) {
                // 1. Buscamos el ID del actor existente
                System.err.println("Advertencia: Actor duplicado encontrado. Buscando ID...");
                // 2. Devolvemos el ID existente para que la Transacción pueda usarlo
                return getIdByNombre(actor.getNombre_actor());
            }
            throw e;
        }
        return -1;
    }

    @Override
    public List<Actor> getActoresPorPeliculaId(int peliculaId) throws SQLException {
        List<Actor> actores = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_PELICULA_ID)) {
            ps.setInt(1, peliculaId);
            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    actores.add(new Actor(
                            rs.getInt("actor_id"),
                            rs.getString("nombre_actor")
                    ));
                }
            }
        }
        return actores;
    }
}
