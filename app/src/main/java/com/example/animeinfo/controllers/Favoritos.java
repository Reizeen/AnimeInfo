package com.example.animeinfo.controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.animeinfo.R;
import com.example.animeinfo.adapters.AdapterAnimes;
import com.example.animeinfo.model.AnimeConstantes;
import com.example.animeinfo.model.ConexionSQLiteHelper;

public class Favoritos extends AppCompatActivity {

    private AdapterAnimes adapterAnimes;
    private RecyclerView recyclerAnimes;
    private ConexionSQLiteHelper conexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        this.setTitle("Favoritos");

        // Conectamos a la BD
        conexion = new ConexionSQLiteHelper(this, AnimeConstantes.NOMBRE_DB, null, 1);

        recyclerAnimes = findViewById(R.id.idRecyclerFavoritos);
        recyclerAnimes.setLayoutManager(new LinearLayoutManager(this));

        adapterAnimes = new AdapterAnimes(this, selectAnimes());
        recyclerAnimes.setAdapter(adapterAnimes);
    }

    /**
     * Visualizar solo los objetos que tengan en favoritos = true
     */

    public Cursor selectAnimes() {
        SQLiteDatabase db = conexion.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " +
                AnimeConstantes.ID + ", " +
                AnimeConstantes.TITULO + ", " +
                AnimeConstantes.ESTRENO + ", " +
                AnimeConstantes.FAVORITO + ", " +
                AnimeConstantes.IMAGEN + ", " +
                AnimeConstantes.URL_WEB + ", " +
                AnimeConstantes.INFO_DESCRIPCION +
                " FROM " + AnimeConstantes.TABLA_ANIME +
                " WHERE " + AnimeConstantes.FAVORITO + " = 1", null);

        return c;
    }
}
