-- Borra las tablas si ya existen (para poder re-ejecutar el script)
DROP TABLE IF EXISTS MiLista;
DROP TABLE IF EXISTS Pelicula_Genero;
DROP TABLE IF EXISTS Pelicula_Actor;
DROP TABLE IF EXISTS Pelicula_Director;
DROP TABLE IF EXISTS Usuario;
DROP TABLE IF EXISTS Pelicula;
DROP TABLE IF EXISTS Clasificacion;
DROP TABLE IF EXISTS Genero;
DROP TABLE IF EXISTS Actor;
DROP TABLE IF EXISTS Director;

-- 1. Tablas Maestras (Entidades en singular)
CREATE TABLE Director (
                          director_id INTEGER PRIMARY KEY AUTOINCREMENT,
                          nombre_director TEXT NOT NULL UNIQUE
);

CREATE TABLE Actor (
                       actor_id INTEGER PRIMARY KEY AUTOINCREMENT,
                       nombre_actor TEXT NOT NULL UNIQUE
);

CREATE TABLE Genero (
                        genero_id INTEGER PRIMARY KEY AUTOINCREMENT,
                        nombre_genero TEXT NOT NULL UNIQUE
);

CREATE TABLE Clasificacion (
                               clasificacion_id INTEGER PRIMARY KEY AUTOINCREMENT,
                               codigo_clasificacion TEXT NOT NULL UNIQUE
);

CREATE TABLE Pelicula (
                          pelicula_id INTEGER PRIMARY KEY AUTOINCREMENT,
                          titulo TEXT NOT NULL,
                          ano_lanzamiento INTEGER,
                          imdb_rating REAL, -- SQLite usa REAL para decimales
                          duracion_min INTEGER,
                          clasificacion_id INTEGER,
                          FOREIGN KEY (clasificacion_id) REFERENCES Clasificacion(clasificacion_id)
);

CREATE TABLE Usuario (
                         usuario_id INTEGER PRIMARY KEY AUTOINCREMENT,
                         nombre_usuario TEXT NOT NULL UNIQUE,
                         email TEXT NOT NULL UNIQUE,
                         password_hash TEXT NOT NULL
);

-- 2. Tablas Intermedias (Relaciones N:M)
CREATE TABLE Pelicula_Director (
                                   pelicula_id INTEGER NOT NULL,
                                   director_id INTEGER NOT NULL,
                                   PRIMARY KEY (pelicula_id, director_id),
                                   FOREIGN KEY (pelicula_id) REFERENCES Pelicula(pelicula_id) ON DELETE CASCADE,
                                   FOREIGN KEY (director_id) REFERENCES Director(director_id) ON DELETE CASCADE
);

CREATE TABLE Pelicula_Actor (
                                pelicula_id INTEGER NOT NULL,
                                actor_id INTEGER NOT NULL,
                                PRIMARY KEY (pelicula_id, actor_id),
                                FOREIGN KEY (pelicula_id) REFERENCES Pelicula(pelicula_id) ON DELETE CASCADE,
                                FOREIGN KEY (actor_id) REFERENCES Actor(actor_id) ON DELETE CASCADE
);

CREATE TABLE Pelicula_Genero (
                                 pelicula_id INTEGER NOT NULL,
                                 genero_id INTEGER NOT NULL,
                                 PRIMARY KEY (pelicula_id, genero_id),
                                 FOREIGN KEY (pelicula_id) REFERENCES Pelicula(pelicula_id) ON DELETE CASCADE,
                                 FOREIGN KEY (genero_id) REFERENCES Genero(genero_id) ON DELETE CASCADE
);

-- 3. Tabla de Usuario (Tu CineLog N:M)
CREATE TABLE MiLista (
                         usuario_id INTEGER NOT NULL,
                         pelicula_id INTEGER NOT NULL,
                         estado TEXT DEFAULT 'PENDIENTE',
                         puntuacion INTEGER,
                         imagen_personal_path TEXT, -- Ruta al archivo de imagen
                         PRIMARY KEY (usuario_id, pelicula_id),
                         FOREIGN KEY (usuario_id) REFERENCES Usuario(usuario_id) ON DELETE CASCADE,
                         FOREIGN KEY (pelicula_id) REFERENCES Pelicula(pelicula_id) ON DELETE CASCADE
);