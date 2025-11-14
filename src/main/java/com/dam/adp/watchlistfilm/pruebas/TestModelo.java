package com.dam.adp.watchlistfilm.pruebas;

import com.dam.adp.watchlistfilm.DAO.DAOFactory;
import com.dam.adp.watchlistfilm.DAO.IUsuarioDAO;
import com.dam.adp.watchlistfilm.model.Usuario;

import java.sql.SQLException;

public class TestModelo {

    // Datos del usuario de prueba
    private static final String TEST_USER = "testUser";
    private static final String TEST_EMAIL = "test@prueba.com";
    private static final String TEST_PASS = "clave123";

    public static void main(String[] args) {

        System.out.println("--- INICIO DE PRUEBAS DEL MODELO USUARIO (POSTGRESQL) ---");

        // 1. Obtener la instancia del DAO a través de la Factory
        // DAOFactory.modoOffline es false por defecto, así que obtendremos UsuarioPostgreSQLImpl
        IUsuarioDAO usuarioDAO = DAOFactory.getUsuarioDAO();

        // --- PRUEBA 1: REGISTRO ---
        try {
            System.out.println("\n[1] Probando REGISTRO de un nuevo usuario...");
            if (usuarioDAO.registro(TEST_USER, TEST_EMAIL, TEST_PASS)) {
                System.out.println("   ✅ ÉXITO: Usuario registrado correctamente.");
            } else {
                System.out.println("   ⚠️ AVISO: El usuario ya existía, continuando con la prueba.");
            }
        } catch (SQLException e) {
            System.err.println("   ❌ ERROR FATAL en Registro: " + e.getMessage());
        }

        // --- PRUEBA 2: LOGIN CORRECTO (Email) ---
        try {
            System.out.println("\n[2] Probando LOGIN CORRECTO (por Email)...");
            Usuario usuarioLogueado = usuarioDAO.loginEmail(TEST_EMAIL, TEST_PASS);

            if (usuarioLogueado != null) {
                System.out.println("   ✅ ÉXITO: Login correcto.");
                System.out.println("   -> Usuario: " + usuarioLogueado.getNombre_usuario());

                // Debe haber llamado al DUMMY de MiListaDAO
                System.out.println("   -> Lista de Películas: " + (usuarioLogueado.getMiListaDePeliculas() != null ? usuarioLogueado.getMiListaDePeliculas().size() + " entradas." : "ERROR."));

                // Comprobación de que el objeto MiLista está conectado al Usuario (bi-direccional)
                if (!usuarioLogueado.getMiListaDePeliculas().isEmpty() && usuarioLogueado.getMiListaDePeliculas().get(0).getUsuario() != null) {
                    System.out.println("   ✅ Conexión bi-direccional (Objeto Usuario) verificada.");
                }

            } else {
                System.out.println("   ❌ FALLO: El login por Email ha fallado. Revisar credenciales/conexión.");
            }
        } catch (SQLException e) {
            System.err.println("   ❌ ERROR FATAL en Login Email: " + e.getMessage());
        }

        // --- PRUEBA 3: LOGIN FALLIDO ---
        try {
            System.out.println("\n[3] Probando LOGIN FALLIDO (Contraseña incorrecta)...");
            Usuario usuarioFallido = usuarioDAO.loginUsuario(TEST_USER, "claveINCORRECTA");

            if (usuarioFallido == null) {
                System.out.println("   ✅ ÉXITO: Login fallido correctamente (Contraseña incorrecta).");
            } else {
                System.out.println("   ❌ FALLO: El login ha devuelto un usuario. ¡FALLO DE SEGURIDAD!");
            }
        } catch (SQLException e) {
            System.err.println("   ❌ ERROR FATAL en Login Fallido: " + e.getMessage());
        }

        System.out.println("\n--- FIN DE PRUEBAS ---");
    }
}