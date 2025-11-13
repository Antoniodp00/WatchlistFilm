package com.dam.adp.watchlistfilm.pruebas;



import com.dam.adp.watchlistfilm.db.ConexionPostgreSQL;
import com.dam.adp.watchlistfilm.db.ConexionSQLite;

import java.sql.Connection;

public class TestConnection {

    public static void main(String[] args) {

        System.out.println("Iniciando pruebas de conexión...");

        // --- Prueba de PostgreSQL (Online) ---
        try {
            System.out.println("Intentando conectar a PostgreSQL (Online)...");
            Connection pgConn = ConexionPostgreSQL.getInstance().getConnection();

            if (pgConn != null && !pgConn.isClosed()) {
                System.out.println("--- ¡ÉXITO! Conexión a PostgreSQL establecida. ---");
                System.out.println("Objeto Conexión: " + pgConn);
                // Cerramos la conexión de prueba
                ConexionPostgreSQL.getInstance().close();
            } else {
                System.out.println("--- ERROR: No se pudo conectar a PostgreSQL. ---");
            }
        } catch (Exception e) {
            System.out.println("--- ERROR FATAL en PostgreSQL: ---");
            e.printStackTrace();
        }

        System.out.println("\n------------------------------------------\n");

        // --- Prueba de SQLite (Offline) ---
        try {
            System.out.println("Intentando conectar a SQLite (Offline)...");
            Connection sqConn = ConexionSQLite.getInstance().getConnection();

            if (sqConn != null && !sqConn.isClosed()) {
                System.out.println("--- ¡ÉXITO! Conexión a SQLite establecida. ---");
                System.out.println("Objeto Conexión: " + sqConn);
                System.out.println(">> Revisa la carpeta raíz de tu proyecto. Deberías ver un nuevo archivo llamado 'CineLog_Offline.db'");
                // Cerramos la conexión de prueba
                ConexionSQLite.getInstance().close();
            } else {
                System.out.println("--- ERROR: No se pudo conectar a SQLite. ---");
            }
        } catch (Exception e) {
            System.out.println("--- ERROR FATAL en SQLite: ---");
            e.printStackTrace();
        }
    }
}