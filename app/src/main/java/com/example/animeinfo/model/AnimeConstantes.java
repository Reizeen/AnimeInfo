package com.example.animeinfo.model;

public class AnimeConstantes {

    // Constantes de los campos de la tabla Animes
    public static final String NOMBRE_DB = "AnimeDB";
    public static final String TABLA_ANIME = "Animes";
    public static final String ID = "_id";
    public static final String TITULO = "titulo";
    public static final String FAVORITO = "favorito";
    public static final String ESTRENO = "estreno";
    public static final String IMAGEN = "imagen";
    public static final String URL_WEB = "url";
    public static final String INFO_DESCRIPCION = "descripcion";

    public static final String IP = "Reizen.pythonanywhere.com";

    /**
     * Numero de la posicion de cada columna en la BD
     */
    public static final int COLUMN_ID = 0;
    public static final int COLUMN_TITULO = 1;
    public static final int COLUMN_ESTRENO = 2;
    public static final int COLUMN_FAVORITO = 3;
    //public static final int COLUMN_IMAGEN = 4;
    public static final int COLUMN_URL = 4;
    public static final int COLUMN_DESCRIPCION = 5;

    //Sentencia SQL para crear la tabla
    public static final String CREAR_TABLA_ANIME = "" +
            "CREATE TABLE " + TABLA_ANIME + "(" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TITULO + " TEXT, " +
            ESTRENO + " TEXT, " +
            FAVORITO + " INTEGER, " +
            IMAGEN + " BLOB, " +
            URL_WEB + " TEXT, " +
            INFO_DESCRIPCION + " TEXT)";
    }
