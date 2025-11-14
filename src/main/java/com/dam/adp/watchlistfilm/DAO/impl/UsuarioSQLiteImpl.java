package com.dam.adp.watchlistfilm.DAO.impl;

import com.dam.adp.watchlistfilm.DAO.IUsuarioDAO;
import com.dam.adp.watchlistfilm.model.Usuario;

import java.sql.SQLException;

public class UsuarioSQLiteImpl implements IUsuarioDAO {
    @Override
    public Usuario loginEmail(String email, String password) throws SQLException {
        return null;
    }

    @Override
    public Usuario loginUsuario(String usuario, String password) throws SQLException {
        return null;
    }

    @Override
    public boolean registro(String nombreUsuario, String email, String password) throws SQLException {
        return false;
    }

    @Override
    public Usuario getUsuarioCompletoPorId(int usuarioId) throws SQLException {
        return null;
    }
}
