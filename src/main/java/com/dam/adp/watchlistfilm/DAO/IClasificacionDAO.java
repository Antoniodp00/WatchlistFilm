package com.dam.adp.watchlistfilm.DAO;

import com.dam.adp.watchlistfilm.model.Clasificacion;
import java.sql.SQLException;

public interface IClasificacionDAO {
    // Métodos de consulta general
    Clasificacion getById(int id) throws SQLException;
    Clasificacion getByCodigo(String codigo) throws SQLException; // Útil para buscar por el código corto (ej: 'UA')

    // Método CRUD (necesario para la Transacción CSV)
    int insertarClasificacion(Clasificacion clasificacion) throws SQLException;
}