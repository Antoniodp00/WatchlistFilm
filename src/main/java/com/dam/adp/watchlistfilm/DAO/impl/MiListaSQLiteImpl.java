package com.dam.adp.watchlistfilm.DAO.impl;

import com.dam.adp.watchlistfilm.DAO.IMiListaDAO;
import com.dam.adp.watchlistfilm.model.MiLista;

import java.sql.SQLException;
import java.util.List;

public class MiListaSQLiteImpl implements IMiListaDAO {
    @Override
    public List<MiLista> getListaCompletaPorUsuarioId(int usuarioId) throws SQLException {
        return List.of();
    }

    @Override
    public boolean addPeliculaALista(int usuarioId, int peliculaId, String estado, int puntuacion) throws SQLException {
        return false;
    }

    @Override
    public boolean updateEntradaMiLista(int usuarioId, int peliculaId, String estado, int puntuacion) throws SQLException {
        return false;
    }

    @Override
    public boolean updateImagen(int usuarioId, int peliculaId, String path) throws SQLException {
        return false;
    }

    @Override
    public boolean deletePeliculaALista(int usuarioId, int peliculaId) throws SQLException {
        return false;
    }
}
