package com.dam.adp.watchlistfilm.DAO;

// Importa todas las implementaciones de PostgreSQL
import com.dam.adp.watchlistfilm.DAO.impl.PeliculaPostgreSQLDAO;
import com.dam.adp.watchlistfilm.DAO.impl.UsuarioPostgreSQLDAO;
import com.dam.adp.watchlistfilm.DAO.impl.MiListaPostgreSQLDAO;
import com.dam.adp.watchlistfilm.DAO.impl.ActorPostgreSQLDAO;
import com.dam.adp.watchlistfilm.DAO.impl.DirectorPostgreSQLDAO;
import com.dam.adp.watchlistfilm.DAO.impl.GeneroPostgreSQLDAO;
import com.dam.adp.watchlistfilm.DAO.impl.ClasificacionPostgreSQLDAO;

/**
 * Factory (Fábrica) para gestionar la creación de objetos DAO.
 * Es el interruptor central para cambiar entre BBDD Online y Offline.
 */
public class DAOFactory {

    public static boolean modoOffline = false;

    /**
     * Devuelve la implementación correcta de IUsuarioDAO (Login/Registro).
     */
    public static IUsuarioDAO getUsuarioDAO() {
        if (modoOffline) {
            // Devuelve la implementación de SQLite (Debe ser creada en el paso final)
            throw new UnsupportedOperationException("Modo Offline (SQLite) aún no implementado.");
        } else {
            return new UsuarioPostgreSQLDAO();
        }
    }

    /**
     * Devuelve la implementación correcta de IPeliculaDAO (Catálogo y Búsquedas).
     */
    public static IPeliculaDAO getPeliculaDAO() {
        if (modoOffline) {
            // return new PeliculaSQLiteDAO();
            throw new UnsupportedOperationException("Modo Offline (SQLite) aún no implementado.");
        } else {
            return new PeliculaPostgreSQLDAO();
        }
    }

    /**
     * Devuelve la implementación correcta de IMiListaDAO (CRUD de la lista de usuario).
     */
    public static IMiListaDAO getMiListaDAO() {
        if (modoOffline) {
            // return new MiListaSQLiteDAO();
            throw new UnsupportedOperationException("Modo Offline (SQLite) aún no implementado.");
        } else {
            return new MiListaPostgreSQLDAO();
        }
    }

    /**
     * Devuelve la implementación correcta de IActorDAO.
     */
    public static IActorDao getActorDAO() {
        if (modoOffline) {
            // return new ActorSQLiteDAO();
            throw new UnsupportedOperationException("Modo Offline (SQLite) aún no implementado.");
        } else {
            return new ActorPostgreSQLDAO();
        }
    }

    /**
     * Devuelve la implementación correcta de IDirectorDAO.
     */
    public static IDirectorDAO getDirectorDAO() {
        if (modoOffline) {
            // return new DirectorSQLiteDAO();
            throw new UnsupportedOperationException("Modo Offline (SQLite) aún no implementado.");
        } else {
            return new DirectorPostgreSQLDAO();
        }
    }

    /**
     * Devuelve la implementación correcta de IGeneroDAO.
     */
    public static IGeneroDAO getGeneroDAO() {
        if (modoOffline) {
            // return new GeneroSQLiteDAO();
            throw new UnsupportedOperationException("Modo Offline (SQLite) aún no implementado.");
        } else {
            return new GeneroPostgreSQLDAO();
        }
    }

    /**
     * Devuelve la implementación correcta de IClasificacionDAO.
     */
    public static IClasificacionDAO getClasificacionDAO() {
        if (modoOffline) {
            // return new ClasificacionSQLiteDAO();
            throw new UnsupportedOperationException("Modo Offline (SQLite) aún no implementado.");
        } else {
            return new ClasificacionPostgreSQLDAO();
        }
    }
}