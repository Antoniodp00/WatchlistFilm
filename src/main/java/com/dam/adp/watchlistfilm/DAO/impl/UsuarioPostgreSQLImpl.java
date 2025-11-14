package com.dam.adp.watchlistfilm.DAO.impl;

import com.dam.adp.watchlistfilm.DAO.IMiListaDAO;
import com.dam.adp.watchlistfilm.DAO.IUsuarioDAO;
import com.dam.adp.watchlistfilm.db.ConexionPostgreSQL;
import com.dam.adp.watchlistfilm.model.MiLista;
import com.dam.adp.watchlistfilm.model.Usuario;
import com.dam.adp.watchlistfilm.DAO.DAOFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioPostgreSQLImpl implements IUsuarioDAO {


    private final String INSERT = "INSERT INTO Usuario (nombre_usuario, email, password_hash) VALUES (?, ?, ?)";
    private final String GETBYID = "SELECT * FROM Usuario WHERE usuario_id = ?";

    private Connection connection;



    public UsuarioPostgreSQLImpl() {

        this.connection = ConexionPostgreSQL.getInstance().getConnection();
    }

    @Override
    public Usuario loginEmail(String email, String password) throws SQLException {
        return loginGenerico("email", email, password);
    }

    @Override
    public Usuario loginUsuario(String usuario, String password) throws SQLException {
        return loginGenerico("nombre_usuario", usuario, password);
    }

    @Override
    public boolean registro(String nombreUsuario, String email, String password) throws SQLException {

        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());

        try(PreparedStatement ps = connection.prepareStatement(INSERT)){
            ps.setString(1, nombreUsuario);
            ps.setString(2, email);
            ps.setString(3, passwordHash);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        }catch (SQLException e){
            if (e.getSQLState().equals("23505")){
                System.err.println("Error: El usuario o email ya existe.");
                return false;
            }else{

                throw e;
            }
        }
    }

    @Override
    public Usuario getUsuarioCompletoPorId(int usuarioId) throws SQLException {
        Usuario usuario = null;

        try(PreparedStatement ps = connection.prepareStatement(GETBYID)){ // 'connection' en minúscula
            ps.setInt(1, usuarioId);
            try(ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    usuario = new Usuario();
                    usuario.setUsuario_id(rs.getInt("usuario_id"));
                    usuario.setNombre_usuario(rs.getString("nombre_usuario"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setPassword_hash(rs.getString("password_hash"));


                    IMiListaDAO miListaDAO = DAOFactory.getMiListaDAO();
                    List<MiLista> lista = miListaDAO.getListaCompletaPorUsuarioId(usuarioId);
                    for (MiLista miLista : lista){
                        miLista.setUsuario(usuario);
                    }
                    usuario.setMiListaDePeliculas(lista);

                }
            }
        }
        return usuario;
    }

    private Usuario loginGenerico(String campoBusqueda, String valorBusqueda, String password) throws SQLException {

        String sql = "SELECT * FROM Usuario WHERE " + campoBusqueda + " = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) { // 'connection' en minúscula
            ps.setString(1, valorBusqueda);
            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()) {
                    String passwordHashBBDD = rs.getString("password_hash");

                    boolean passwordCorrecta = BCrypt.checkpw(password, passwordHashBBDD);

                    if (passwordCorrecta){
                        int usuarioId = rs.getInt("usuario_id");
                        return getUsuarioCompletoPorId(usuarioId);
                    }
                }
            }
        }
        return null;
    }
}