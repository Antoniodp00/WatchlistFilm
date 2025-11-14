package com.dam.adp.watchlistfilm.DAO;

import com.dam.adp.watchlistfilm.model.MiLista;

import java.sql.SQLException;
import java.util.List;

public interface IMiListaDAO {

    List<MiLista> getListaCompletaPorUsuarioId(int usuarioId) throws SQLException;
    boolean addPeliculaALista(int usuarioId, int peliculaId, String estado, int puntuacion) throws SQLException;
    boolean updateEntradaMiLista(int usuarioId, int peliculaId, String estado, int puntuacion) throws SQLException;

    boolean updateImagen(int usuarioId, int peliculaId, String path) throws SQLException;
    boolean deletePeliculaALista(int usuarioId, int peliculaId) throws SQLException;
}
