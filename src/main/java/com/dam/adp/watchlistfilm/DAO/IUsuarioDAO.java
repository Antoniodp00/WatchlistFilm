package com.dam.adp.watchlistfilm.DAO;

import com.dam.adp.watchlistfilm.model.Usuario;

import java.sql.SQLException;

public interface IUsuarioDAO {

    Usuario loginEmail(String email, String password) throws SQLException;
    Usuario loginUsuario(String usuario, String password) throws SQLException;

    boolean registro(String nombreUsuario,String email,String password) throws SQLException;

    Usuario getUsuarioCompletoPorId(int usuarioId) throws SQLException;
}
