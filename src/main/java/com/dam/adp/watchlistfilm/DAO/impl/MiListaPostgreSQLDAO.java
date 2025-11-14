package com.dam.adp.watchlistfilm.DAO.impl;

import com.dam.adp.watchlistfilm.DAO.IMiListaDAO;
import com.dam.adp.watchlistfilm.DAO.IPeliculaDAO;
import com.dam.adp.watchlistfilm.DAO.IUsuarioDAO;
import com.dam.adp.watchlistfilm.db.ConexionPostgreSQL;
import com.dam.adp.watchlistfilm.model.MiLista;
import com.dam.adp.watchlistfilm.DAO.DAOFactory;
import com.dam.adp.watchlistfilm.model.Pelicula;
import com.dam.adp.watchlistfilm.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MiListaPostgreSQLDAO implements IMiListaDAO {

    private static final String SELECT_BY_USER_ID = "SELECT * FROM MiLista WHERE usuario_id = ?";
    private static final String INSERT = "INSERT INTO MiLista (usuario_id, pelicula_id, estado, puntuacion) VALUES (?, ?, 'PENDIENTE', null)";
    private static final String UPDATE_STATUS_SCORE = "UPDATE MiLista SET estado = ?, puntuacion = ? WHERE usuario_id = ? AND pelicula_id = ?";
    private static final String UPDATE_IMAGE = "UPDATE MiLista SET imagen_personal_path = ? WHERE usuario_id = ? AND pelicula_id = ?";
    private static final String DELETE = "DELETE FROM MiLista WHERE usuario_id = ? AND pelicula_id = ?";

    private Connection connection;

    public MiListaPostgreSQLDAO() {
        this.connection = ConexionPostgreSQL.getInstance().getConnection();
    }


    @Override
    public List<MiLista> getListaCompletaPorUsuarioId(int usuarioId) throws SQLException {
        List<MiLista> lista = new ArrayList<>();

        IPeliculaDAO peliculaDAO = DAOFactory.getPeliculaDAO();
        IUsuarioDAO usuarioDAO = DAOFactory.getUsuarioDAO();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_USER_ID)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int peliculaId = rs.getInt("pelicula_id");

                    Pelicula pelicula = peliculaDAO.getPeliculaCompletaPorId(peliculaId);
                    Usuario usuario = usuarioDAO.getUsuarioCompletoPorId(usuarioId);

                    MiLista miLista = new MiLista();
                    miLista.setUsuario(usuario);
                    miLista.setPelicula(pelicula);
                    miLista.setEstado(rs.getString("estado"));
                    miLista.setPuntuacion(rs.getInt("puntuacion"));
                    miLista.setImagen_personal_path(rs.getString("imagen_personal_path"));

                    lista.add(miLista);
                }
            }
        }
        return lista;
    }

    @Override
    public boolean addPeliculaALista(int usuarioId, int peliculaId, String estado, int puntuacion) throws
            SQLException {
        try (PreparedStatement ps = connection.prepareStatement(INSERT)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, peliculaId);
            return ps.executeUpdate() > 0;
        }catch (SQLException e){
            if (e.getSQLState().equals("23505")){
                System.err.println("Error: La película ya está en la lista.");
                return false;
            }else{
                throw e;
            }
        }
    }

    @Override
    public boolean updateEntradaMiLista(int usuarioId, int peliculaId, String estado, int puntuacion) throws
            SQLException {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE_STATUS_SCORE)) {
            ps.setString(1, estado);
            ps.setInt(2, puntuacion);
            ps.setInt(3, usuarioId);
            ps.setInt(4, peliculaId);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateImagen(int usuarioId, int peliculaId, String path) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE_IMAGE)) {
            ps.setString(1, path);
            ps.setInt(2, usuarioId);
            ps.setInt(3, peliculaId);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deletePeliculaALista(int usuarioId, int peliculaId) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(DELETE)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, peliculaId);
            return ps.executeUpdate() > 0;
        }
    }
}
