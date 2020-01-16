package com.example.animeinfo.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {

    public ConexionSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * Generar las tablas
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(AnimeConstantes.CREAR_TABLA_ANIME);
    }

    /**
     * Cada vez que se ejecuta la aplicacion verifica si existe antes
     * una version antigua de la base de datos, actualizará la base de datos
     * si existe una version nueva.
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // NOTA: Por simplicidad del ejemplo aquí utilizamos directamente la opción de
        // eliminar la tabla anterior y crearla de nuevo vacía con el nuevo formato.
        // Sin embargo lo normal será que haya que migrar datos de la tabla antigua
        // a la nueva, por lo que este método debería ser más elaborado.

        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Animes");
        onCreate(db);
    }
}
