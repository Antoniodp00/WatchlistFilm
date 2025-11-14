package com.dam.adp.watchlistfilm.DAO;

// Importa las implementaciones que VAMOS a crear
import com.dam.adp.watchlistfilm.DAO.IMiListaDAO;
import com.dam.adp.watchlistfilm.DAO.IPeliculaDAO;
import com.dam.adp.watchlistfilm.DAO.IUsuarioDAO;
import com.dam.adp.watchlistfilm.DAO.impl.PeliculaPostgreSQLImpl;
import com.dam.adp.watchlistfilm.DAO.impl.PeliculaSQLiteImpl;
import com.dam.adp.watchlistfilm.DAO.impl.UsuarioPostgreSQLImpl;
import com.dam.adp.watchlistfilm.DAO.impl.UsuarioSQLiteImpl;
import com.dam.adp.watchlistfilm.DAO.impl.MiListaPostgreSQLImpl;
import com.dam.adp.watchlistfilm.DAO.impl.MiListaSQLiteImpl;

/**
 * Factory (Fábrica) para gestionar la creación de objetos DAO.
 * Esta es la clase clave que permite cambiar entre el modo Online (PostgreSQL)
 * y el modo Offline (SQLite) sin que el Controlador se entere.
 */
public class DAOFactory {

    // Esta variable estática será el interruptor principal de la app.
    // El LoginController la cambiará a 'true' si el usuario marca "Modo Offline".
    public static boolean modoOffline = false;

    /**
     * Devuelve la implementación correcta de IUsuarioDAO según el modo.
     * @return un objeto que implementa IUsuarioDAO.
     */
    public static IUsuarioDAO getUsuarioDAO() {
        if (modoOffline) {
            // Devuelve la implementación de SQLite
            return new UsuarioSQLiteImpl();
        } else {
            // Devuelve la implementación de PostgreSQL
            return new UsuarioPostgreSQLImpl();
        }
    }

    /**
     * Devuelve la implementación correcta de IPeliculaDAO según el modo.
     * @return un objeto que implementa IPeliculaDAO.
     */
    public static IPeliculaDAO getPeliculaDAO() {
        if (modoOffline) {
            return new PeliculaSQLiteImpl();
        } else {
            return new PeliculaPostgreSQLImpl();
        }
    }

    /**
     * Devuelve la implementación correcta de IMiListaDAO según el modo.
     * @return un objeto que implementa IMiListaDAO.
     */
    public static IMiListaDAO getMiListaDAO() {
        if (modoOffline) {
            return new MiListaSQLiteImpl();
        } else {
            return new MiListaPostgreSQLImpl();
        }
    }

    // (Opcional: Si la importación CSV es muy compleja,
    // podría tener su propio DAO/Servicio y ser gestionada aquí también)
}
