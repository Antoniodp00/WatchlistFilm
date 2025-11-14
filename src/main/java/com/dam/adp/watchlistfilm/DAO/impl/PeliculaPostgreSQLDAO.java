package com.dam.adp.watchlistfilm.DAO.impl;

import com.dam.adp.watchlistfilm.DAO.*; // Importamos todas las Interfaces
import com.dam.adp.watchlistfilm.db.ConexionPostgreSQL;
import com.dam.adp.watchlistfilm.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PeliculaPostgreSQLDAO implements IPeliculaDAO {

    private Connection connection;

    // --- DEPENDENCIAS INYECTADAS ---
    private final IActorDao actorDAO;
    private final IDirectorDAO directorDAO;
    private final IGeneroDAO generoDAO;
    private final IClasificacionDAO clasificacionDAO;

    // SQL PARA LA INSERCIÓN TRANSACCIONAL (CRUD: CREATE)
    private static final String INSERT_PELICULA_SQL =
            "INSERT INTO Pelicula (titulo, ano_lanzamiento, imdb_rating, duracion_min, clasificacion_id) " +
                    "VALUES (?, ?, ?, ?, ?) RETURNING pelicula_id";
    private static final String LINK_ACTOR_SQL =
            "INSERT INTO Pelicula_Actor (pelicula_id, actor_id) VALUES (?, ?)";
    private static final String LINK_DIRECTOR_SQL =
            "INSERT INTO Pelicula_Director (pelicula_id, director_id) VALUES (?, ?)";
    private static final String LINK_GENERO_SQL =
            "INSERT INTO Pelicula_Genero (pelicula_id, genero_id) VALUES (?, ?)";

    // --- CONSULTAS SQL (LECTURA) ---
    private static final String SELECT_PELICULA_BY_ID="Select * FROM Pelicula WHERE pelicula_id = ?";
    private static final String SEARCH_BY_TITLE_SQL = "SELECT pelicula_id FROM Pelicula WHERE titulo ILIKE ?";
    private static final String SEARCH_BY_CLASSIFICATION_SQL = "SELECT p.pelicula_id FROM Pelicula p JOIN Clasificacion c ON p.clasificacion_id = c.clasificacion_id WHERE c.codigo_clasificacion ILIKE ?";
    private static final String SEARCH_BY_GENRE_SQL = "SELECT DISTINCT p.pelicula_id FROM Pelicula p JOIN Pelicula_Genero pg ON p.pelicula_id = pg.pelicula_id JOIN Genero g ON pg.genero_id = g.genero_id WHERE g.nombre_genero ILIKE ?";
    private static final String SEARCH_BY_DIRECTOR_SQL = "SELECT DISTINCT p.pelicula_id FROM Pelicula p JOIN Pelicula_Director pd ON p.pelicula_id = pd.pelicula_id JOIN Director d ON pd.director_id = d.director_id WHERE d.nombre_director ILIKE ?";
    private static final String SEARCH_BY_ACTOR_SQL = "SELECT DISTINCT p.pelicula_id FROM Pelicula p JOIN Pelicula_Actor pa ON p.pelicula_id = pa.pelicula_id JOIN Actor a ON pa.actor_id = a.actor_id WHERE a.nombre_actor ILIKE ?";


    public PeliculaPostgreSQLDAO() {
        this.connection = ConexionPostgreSQL.getInstance().getConnection();

        this.actorDAO = DAOFactory.getActorDAO();
        this.directorDAO = DAOFactory.getDirectorDAO();
        this.generoDAO = DAOFactory.getGeneroDAO();
        this.clasificacionDAO = DAOFactory.getClasificacionDAO();
    }

    // Método auxiliar para vincular IDs en tablas N:M
    private void linkIds(int peliculaId, int entidadId, String sql) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, peliculaId);
            ps.setInt(2, entidadId);
            ps.executeUpdate();
        }
    }

    /**
     * Inserta una película COMPLETA (con actores, directores, géneros y clasificación)
     * asegurando la integridad referencial. Esto es una TRANSACCIÓN.
     * @param pelicula El objeto Pelicula con las listas llenas.
     * @return El ID de la película creada.
     * @throws SQLException Si algo falla, se hace ROLLBACK.
     */
    public int insertarPeliculaCompleta(Pelicula pelicula) throws SQLException {
        int peliculaId = -1;

        // 1. Desactivar el Auto-Commit para iniciar la Transacción
        connection.setAutoCommit(false);

        try (PreparedStatement ps = connection.prepareStatement(INSERT_PELICULA_SQL, Statement.RETURN_GENERATED_KEYS)) {

            // 2. Insertar la Clasificación (la 1:N)
            // Obtenemos el ID de la clasificación (o la insertamos si es nueva)
            int clasificacionId = clasificacionDAO.insertarClasificacion(pelicula.getClasificacion());

            if (clasificacionId == -1) throw new SQLException("Fallo al obtener Clasificacion ID."); // Comprobación

            // 3. Insertar la Película principal
            ps.setString(1, pelicula.getTitulo());
            ps.setInt(2, pelicula.getAno_lanzamiento());
            ps.setDouble(3, pelicula.getImdb_rating());
            ps.setInt(4, pelicula.getDuracion_min());
            ps.setInt(5, clasificacionId);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    peliculaId = rs.getInt(1);
                } else {
                    throw new SQLException("Fallo al obtener el ID de la película.");
                }
            }

            // 4. Insertar las N:M (Actores, Directores, Géneros)

            // A. Insertar/Vincular Actores
            for (Actor actor : pelicula.getActores()) {
                int actorId = actorDAO.insertarActor(actor); // Inserta si no existe, devuelve ID existente o nuevo
                if (actorId == -1) throw new SQLException("Fallo al obtener ID de Actor.");
                linkIds(peliculaId, actorId, LINK_ACTOR_SQL); // Inserta en tabla Pelicula_Actor
            }

            // B. Insertar/Vincular Directores
            for (Director director : pelicula.getDirectores()) {
                int directorId = directorDAO.insertarDirector(director);
                if (directorId == -1) throw new SQLException("Fallo al obtener ID de Director.");
                linkIds(peliculaId, directorId, LINK_DIRECTOR_SQL); // Inserta en tabla Pelicula_Director
            }

            // C. Insertar/Vincular Géneros
            for (Genero genero : pelicula.getGeneros()) {
                int generoId = generoDAO.insertarGenero(genero);
                if (generoId == -1) throw new SQLException("Fallo al obtener ID de Género.");
                linkIds(peliculaId, generoId, LINK_GENERO_SQL); // Inserta en tabla Pelicula_Genero
            }

            // 5. Commit (Todo fue bien)
            connection.commit();

        } catch (SQLException e) {
            // 6. Rollback (Algo falló)
            connection.rollback();
            throw e;
        } finally {
            // 7. Restaurar el Auto-Commit
            connection.setAutoCommit(true);
        }

        return peliculaId;
    }

    @Override
    public Pelicula getPeliculaCompletaPorId(int id) throws SQLException {

        Pelicula pelicula = null;
        Clasificacion clasificacion = null;

        try (PreparedStatement ps = connection.prepareStatement(SELECT_PELICULA_BY_ID)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // 1. Crear el objeto Clasificacion (1:N)
                    clasificacion = new Clasificacion(
                            rs.getInt("clasificacion_id"),
                            rs.getString("codigo_clasificacion")
                    );

                    // 2. Crear y rellenar el objeto Pelicula (base)
                    pelicula = new Pelicula();
                    pelicula.setPelicula_id(rs.getInt("pelicula_id"));
                    pelicula.setTitulo(rs.getString("titulo"));
                    pelicula.setAno_lanzamiento(rs.getInt("ano_lanzamiento"));
                    pelicula.setImdb_rating(rs.getDouble("imdb_rating"));
                    pelicula.setDuracion_min(rs.getInt("duracion_min"));
                    pelicula.setClasificacion(clasificacion);

                    // 3. Cargar y asignar las listas (N:M) a través de los DAOs especializados
                    pelicula.setActores(actorDAO.getActoresPorPeliculaId(id));
                    pelicula.setDirectores(directorDAO.getDirectoresPorPeliculaId(id));
                    pelicula.setGeneros(generoDAO.getGenerosPorPeliculaId(id));
                }
            }
        }
        return pelicula;
    }

    @Override
    public List<Pelicula> getAllPeliculas() throws SQLException {
        List<Pelicula> peliculas = new ArrayList<>();
        String sql = "SELECT pelicula_id FROM Pelicula ORDER BY pelicula_id ASC";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int peliculaId = rs.getInt("pelicula_id");
                Pelicula p = getPeliculaCompletaPorId(peliculaId);
                if (p != null) {
                    peliculas.add(p);
                }
            }
        }
        return peliculas;
    }

    private List<Pelicula> fetchPeliculasByIds(String sqlQuery, String parametro) throws SQLException {
        List<Pelicula> peliculas = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setString(1, "%" + parametro + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pelicula p = getPeliculaCompletaPorId(rs.getInt("pelicula_id"));
                    if (p != null) {
                        peliculas.add(p);
                    }
                }
            }
        }
        return peliculas;
    }

    @Override
    public List<Pelicula> bucarPeliculaPorTitulo(String titulo) throws SQLException {
        return fetchPeliculasByIds(SEARCH_BY_TITLE_SQL, titulo);
    }

    @Override
    public List<Pelicula> bucarPeliculaPorClasificacion(String clasificacion) throws SQLException {
        return fetchPeliculasByIds(SEARCH_BY_CLASSIFICATION_SQL, clasificacion);
    }

    @Override
    public List<Pelicula> bucarPeliculaPorGenero(String genero) throws SQLException {
        return fetchPeliculasByIds(SEARCH_BY_GENRE_SQL, genero);
    }

    @Override
    public List<Pelicula> bucarPeliculaPorDirector(String director) throws SQLException {
        return fetchPeliculasByIds(SEARCH_BY_DIRECTOR_SQL, director);
    }

    @Override
    public List<Pelicula> bucarPeliculaPorActor(String actor) throws SQLException {
        return fetchPeliculasByIds(SEARCH_BY_ACTOR_SQL, actor);
    }
}