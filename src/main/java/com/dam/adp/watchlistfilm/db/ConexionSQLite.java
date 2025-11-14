package com.dam.adp.watchlistfilm.db; // (Tu paquete)

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class ConexionSQLite {

    private static ConexionSQLite instancia;
    private Connection connection;
    private static final String URL = "jdbc:sqlite:CineLog_Offline.db";

    // 2. Constructor privado
    private ConexionSQLite() {
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection(URL);
            System.out.println("Conexión a SQLite (Offline) establecida.");

            this.crearTablasSiNoExisten(this.connection);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al conectar con SQLite", e);
        }
    }


    public static synchronized ConexionSQLite getInstance() {
        if (instancia == null) {
            instancia = new ConexionSQLite();
        }
        return instancia;
    }

    /**
     * Lee el archivo 'sqlite_schema.sql' de resources y ejecuta los comandos
     * para crear la estructura de tablas.
     */
    private void crearTablasSiNoExisten(Connection conn) {
        // Usamos 'try-with-resources' para asegurarnos de que todo se cierra
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("sqlite_schema.sql")) {

            if (is == null) {
                System.err.println("¡Error! No se encontró el archivo sqlite_schema.sql en resources.");
                return;
            }

            // Leemos el archivo SQL entero en un solo String
            String scriptSQL;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                scriptSQL = reader.lines().collect(Collectors.joining("\n"));
            }

            try (Statement stmt = conn.createStatement()) {
                // Separamos el script por cada punto y coma
                String[] comandos = scriptSQL.split(";");

                for (String comando : comandos) {

                    // Limpiamos el comando: quitamos comentarios (--) y espacios
                    String sql = comando.replaceAll("--.*", "").trim();

                    // Si el comando (ya limpio) no está vacío, lo ejecutamos
                    if (!sql.isEmpty()) {
                        stmt.execute(sql);
                    }
                }
                System.out.println("Tablas de SQLite (Offline) verificadas/creadas con éxito.");
            }

        } catch (Exception e) {
            System.err.println("Error al ejecutar el script de creación de BBDD SQLite.");
            e.printStackTrace();

            throw new RuntimeException("Fallo al inicializar la BBDD SQLite", e);
        }
    }
    public Connection getConnection() {
        return this.connection;
    }
    public void close() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
                System.out.println("Conexión a SQLite cerrada.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}