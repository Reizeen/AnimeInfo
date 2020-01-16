package com.example.animeinfo.model;

public class AnimeConstantes {

    // Constantes de los campos de la tabla Animes
    public static final String NOMBRE_DB = "AnimeDB";
    public static final String TABLA_ANIME = "Animes";
    public static final String ID = "id";
    public static final String TITULO = "titulo";
    public static final String FAVORITO = "favorito";
    public static final String ESTRENO = "estreno";
    public static final String FOTO = "foto";
    public static final String URL_WEB = "url";
    public static final String INFO_DESCRIPCION = "descripcion";

    //Sentencia SQL para crear la tabla
    public static final String CREAR_TABLA_ANIME = "" +
            "CREATE TABLE " + TABLA_ANIME + "(" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TITULO + " TEXT, " +
            ESTRENO + " TEXT, " +
            FAVORITO + " INTEGER, " +
            FOTO + " TEXT, " +
            URL_WEB + " TEXT, " +
            INFO_DESCRIPCION + " TEXT)";
    }
