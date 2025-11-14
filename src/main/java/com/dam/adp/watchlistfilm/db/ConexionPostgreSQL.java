package com.dam.adp.watchlistfilm.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionPostgreSQL {


    private static ConexionPostgreSQL instancia;

    private static Connection connection;
    private Properties properties;


    private ConexionPostgreSQL() {
        try {
            // Cargar propiedades desde el archivo config.properties
            properties = new Properties();
            InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties");
            if (is == null) {
                throw new RuntimeException("No se pudo encontrar el archivo config.properties");
            }
            properties.load(is);

            // Registrar el driver (aunque con JDBC 4+ suele ser automático)
            Class.forName("org.postgresql.Driver");

            // Establecer la conexión
            this.connection = DriverManager.getConnection(
                    properties.getProperty("db.url"),
                    properties.getProperty("db.user"),
                    properties.getProperty("db.password")
            );
            System.out.println("Conexión a PostgreSQL (Online) establecida.");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al conectar con PostgreSQL", e);
        }
    }


    public static synchronized ConexionPostgreSQL getInstance() {
        if (instancia == null) {
            instancia = new ConexionPostgreSQL();
        }
        return instancia;
    }


    public Connection getConnection() {
        return this.connection;
    }


    public void close() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
                System.out.println("Conexión a PostgreSQL cerrada.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}