package com.example.animeinfo.model;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

public class AnimesProvider extends ContentProvider {

    /**
     * Definición del CONTENT_URI
     */
    private static final String uri =
            "content://com.example.animeinfo.contentproviders/animes";

    public static final Uri CONTENT_URI = Uri.parse(uri);


    /**
     * Existen columnas predefinidas en los content providers,
     * por ejemplo la columna _ID.
     * Esta clase interna sirve para declarar las constantes de
     * las columnas que nos hacen falta.
     */
    public static final class Animes implements BaseColumns {
        private Animes() {}

        // Nombres de columnas
        public static final String COL_TITULO = AnimeConstantes.TITULO;
        public static final String COL_ESTRENO = AnimeConstantes.ESTRENO;
        public static final String COL_DESCRIPCION = AnimeConstantes.INFO_DESCRIPCION;
    }

    /**
     *  Definir varios atributos para almacenar la conexion y
     *  la versión de la base de datos.
     */
    private ConexionSQLiteHelper conexion;
    private static final int BD_VERSION = 1;

    /**
     * Con UriMatcher será útil determinar si una URI hace referencia a la tabla generica
     * o a un registro concreto a través de su ID.
     */
    private static final int ANIMES = 1;
    private static final int ANIMES_ID = 2;
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.example.animeinfo.contentproviders", "animes", ANIMES);
        uriMatcher.addURI("com.example.animeinfo.contentproviders", "animes/#", ANIMES_ID);
    }

    @Override
    public boolean onCreate() {
        conexion = new ConexionSQLiteHelper(
                getContext(), AnimeConstantes.NOMBRE_DB, null, BD_VERSION);
        return true;
    }


    /**
     * Devuelve los datos solicitados según la URI indicada y los
     *criterios de selección y ordenación pasados como parámetro.
     *
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        //Si es una consulta a un ID concreto construimos el WHERE
        String where = selection;

        if(uriMatcher.match(uri) == ANIMES_ID)
            where = AnimeConstantes.ID + uri.getLastPathSegment();

        SQLiteDatabase db = conexion.getWritableDatabase();

        Cursor c = db.query(AnimeConstantes.TABLA_ANIME, projection, where,
                selectionArgs, null, null, sortOrder);

        return c;
    }


    /**
     * Identifica el tipo de dato que devuelve el content provider
     * El tipo de dato se expresará con un MIME Type.
     * al igual que hacen los navegadores web para determinar
     * el tipo de datos que están recibiendo tras una petición a un servidor
     * @param uri
     * @return
     */
    @Override
    public String getType(Uri uri) {
        int match = uriMatcher.match(uri);

        switch (match) {
            case ANIMES:
                return "vnd.android.cursor.dir/vnd.animeinfo.anime";
            case ANIMES_ID:
                return "vnd.android.cursor.item/vnd.animeinfo.anime";
            default:
                return null;
        }
    }

    /**
     * Devuelve la URI que hace referencia al nuevo registro insertado
     * @param uri
     * @param values
     * @return
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long regId = 1;

        SQLiteDatabase db = conexion.getWritableDatabase();

        regId = db.insert(AnimeConstantes.TABLA_ANIME, null, values);

        Uri newUri = ContentUris.withAppendedId(CONTENT_URI, regId);

        return newUri;
    }

    /**
     * Devuelve el numero de registros borrados
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int cont;

        // Si es una consulta a un ID concreto construimos el WHERE
        String where = selection;
        if(uriMatcher.match(uri) == ANIMES_ID)
            where = "_id=" + uri.getLastPathSegment();

        SQLiteDatabase db = conexion.getWritableDatabase();

        cont = db.delete(AnimeConstantes.TABLA_ANIME, where, selectionArgs);

        return cont;
    }

    /**
     * Devuelve el numero de rigstros modificados
     * @param uri
     * @param values
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int cont;

        //Si es una consulta a un ID concreto construimos el WHERE
        String where = selection;
        if(uriMatcher.match(uri) == ANIMES_ID)
            where = "_id=" + uri.getLastPathSegment();

        SQLiteDatabase db = conexion.getWritableDatabase();

        cont = db.update(AnimeConstantes.TABLA_ANIME, values, where, selectionArgs);

        return cont;
    }
}
