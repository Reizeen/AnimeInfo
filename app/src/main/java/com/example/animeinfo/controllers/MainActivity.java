package com.example.animeinfo.controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.animeinfo.R;
import com.example.animeinfo.adapters.AdapterAnimes;
import com.example.animeinfo.model.Anime;
import com.example.animeinfo.model.AnimeConstantes;
import com.example.animeinfo.model.ConexionSQLiteHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AdapterAnimes adapterAnimes;
    private RecyclerView recyclerAnimes;
    private ConexionSQLiteHelper conexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Conectamos a la BD
        conexion = new ConexionSQLiteHelper(this, AnimeConstantes.NOMBRE_DB, null, 1);

        recyclerAnimes = findViewById(R.id.idRecyclerView);
        recyclerAnimes.setLayoutManager(new LinearLayoutManager(this));

        adapterAnimes = new AdapterAnimes(this, selectAnimes());
        recyclerAnimes.setAdapter(adapterAnimes);

        abrePerfilAnime(adapterAnimes);
    }

    /**
     * Crear Menu del MainActivity
     * Crear Buscador para el menu
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_buscar);

        // Asignar un SearchView al boton del action
        SearchView search = (SearchView) item.getActionView();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Separar la palabra del buscador
                newText = newText.toLowerCase();

                /* Si no se está  buscando nada se actualiza la lista actual
                 * sino se actualiza la lista filtrada. */
                if (newText.isEmpty()){
                    adapterAnimes.swapCursor(selectAnimes());
                } else {
                    adapterAnimes.swapCursor(selectAnimesWhere(newText));
                }
                return true;
            }
        });
        return true;
    }

    /**
     * Consultar todos los animes de la BD
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
                AnimeConstantes.INFO_DESCRIPCION + " " +
                "FROM " + AnimeConstantes.TABLA_ANIME, null);

        return c;
    }

    /**
     * Consultar los animes de la BD segun el where
     */
    public Cursor selectAnimesWhere(String titulo) {
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
                " WHERE " + AnimeConstantes.TITULO + " LIKE('" + titulo + "%')", null);

        return c;
    }

    /**
     * Metodo onClick del menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_anadir:
                abreCrearAnime();
                return true;
            case R.id.action_favoritos:
                abreFavoritos();
                return true;
            case R.id.action_llamar:
                llamarTienda();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Llamar por telefono a la tienda
     */
    private void llamarTienda() {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:672629147")));
    }

    /**
     * Abre la actividad del perfil de un item del RecyclerView
     */
    public void abrePerfilAnime(final AdapterAnimes adapterAnimes) {
        adapterAnimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Anime anime = adapterAnimes.obtenerAnime(recyclerAnimes.getChildAdapterPosition(view));
                Intent intencion = new Intent(getApplicationContext(), PerfilAnime.class);
                intencion.putExtra("anime", anime);
                startActivityForResult(intencion, 101);
            }
        });
    }

    /**
     * Abre la actividad de añadir un nuevo Anime
     */
    public void abreCrearAnime() {
        Intent intencion = new Intent(getApplicationContext(), AddAnime.class);
        startActivityForResult(intencion, 102);
    }

    /**
     * Abre la actividad de Favoritos
     */
    private void abreFavoritos() {
        Intent intent = new Intent(MainActivity.this, Favoritos.class);
        startActivityForResult(intent, 103);
    }

    /**
     * Guardar la informacion de las otras actividades
     */
    public void onActivityResult(int requestCode, int resultCode, Intent code) {
        if (requestCode == 101 && resultCode == RESULT_OK) {
            Anime animeMod = (Anime) code.getSerializableExtra("anime");
            int operacionCode = code.getIntExtra("operacionCode", -1);

            if (operacionCode == 0)
                modificarFavorito(animeMod);
            else if (operacionCode == -1)
                eliminarAnime(animeMod);

        } else if (requestCode == 102 && resultCode == RESULT_OK) {
            Toast.makeText(getApplicationContext(), "Anime insertado correctamente", Toast.LENGTH_SHORT).show();
        }

        adapterAnimes.swapCursor(selectAnimes());
    }

    /**
     * Eliminar anime de la BD despues de volver del perifl
     */
    public void eliminarAnime(Anime animeMod) {
        SQLiteDatabase db = conexion.getReadableDatabase();
        db.execSQL("DELETE FROM " + AnimeConstantes.TABLA_ANIME + " WHERE " + AnimeConstantes.ID + " = " + animeMod.getId());
        Toast.makeText(getApplicationContext(), "Anime eliminado correctamente", Toast.LENGTH_SHORT).show();
    }

    /**
     * Modificar favorito de un anime en la BD despues de volver del perfil
     */
    public void modificarFavorito(Anime animeMod) {
        int numFav;
        String mensaje;
        if (animeMod.getFavorito()) {
            numFav = 1;
            mensaje = "Anime añadido a favoritos";
        } else {
            numFav = 0;
            mensaje = "Anime eliminado de favoritos";
        }

        //Actualizamos favorito en la base de datos
        SQLiteDatabase db = conexion.getReadableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(AnimeConstantes.FAVORITO, numFav);
        db.update(AnimeConstantes.TABLA_ANIME, valores, AnimeConstantes.ID + " = " + animeMod.getId(), null);
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }



}