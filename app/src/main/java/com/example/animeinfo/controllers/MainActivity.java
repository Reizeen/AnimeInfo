package com.example.animeinfo.controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private AdapterAnimes adapterAnimes;
    private ArrayList<Anime> listaAnimes;
    private RecyclerView recyclerAnimes;
    private ConexionSQLiteHelper conexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creamos la base de datos
        conexion = new ConexionSQLiteHelper(this, AnimeConstantes.NOMBRE_DB, null, 1);

        listaAnimes = new ArrayList<>();
        recyclerAnimes = findViewById(R.id.idRecyclerView);
        recyclerAnimes.setLayoutManager(new LinearLayoutManager(this));

        adapterAnimes = new AdapterAnimes(listaAnimes);
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
        SearchView search = (SearchView) MenuItemCompat.getActionView(item);
        // Que el buscador escuche en este contesto
        search.setOnQueryTextListener(this);
        /* Los metodos sirven para cuando no tengamos ningun texto en el buscador la lsita de
         * Animes quedará como estaba antes. */
        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                adapterAnimes.setFilter(listaAnimes);
                return true;
            }
        });

        return true;
    }

    /**
     * Consultar los animes de la BD
     */

    public void selectAnimes(){
        SQLiteDatabase db = conexion.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " +
                AnimeConstantes.ID + ", " +
                AnimeConstantes.TITULO + ", " +
                AnimeConstantes.FAVORITO + ", " +
                AnimeConstantes.ESTRENO + ", " +
                AnimeConstantes.FOTO + ", " +
                AnimeConstantes.URL_WEB + ", " +
                AnimeConstantes.INFO_DESCRIPCION + " " +
                "FROM " + AnimeConstantes.TABLA_ANIME, null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                // Crear un amime con las consulta y meterlo en el arrayList
            } while(c.moveToNext());
        }
    }

    /**
     *Metodo onClick del menu
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
    public void abrePerfilAnime(AdapterAnimes adapterAnimes){
        adapterAnimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Anime anime = listaAnimes.get(recyclerAnimes.getChildAdapterPosition(view));
                Intent intencion = new Intent(getApplicationContext(), PerfilAnime.class);
                intencion.putExtra("anime", anime);
                startActivityForResult(intencion, 101);
            }
        });
    }

    /**
     * Abre la actividad del perfil de un item del RecyclerView
     */
    public void abreCrearAnime(){
        Intent intencion = new Intent(getApplicationContext(), AddAnime.class);
        startActivityForResult(intencion, 102);
    }

    /**
     * Abre la actividad de Favoritos
     */
    private void abreFavoritos() {
        Intent intent = new Intent(MainActivity.this, Favoritos.class);
        intent.putExtra("listaAnime", listaAnimes);
        startActivityForResult(intent, 103);
    }

    /**
     * Guardar la informacion de las otras actividades
     */
    public void onActivityResult(int requestCode, int resultCode, Intent code){
        if (requestCode == 101 && resultCode == RESULT_OK){
            Anime animeMod = (Anime) code.getSerializableExtra("anime");
            for (Anime anime : listaAnimes) {
                if (anime.getId() == animeMod.getId()){
                    anime.setFavorito(animeMod.getFavorito());
                }
            }
        } else if (requestCode == 102 && resultCode == RESULT_OK) {
            listaAnimes.clear();
            Toast.makeText(getApplicationContext(), "Anime insertado correctamente", Toast.LENGTH_SHORT).show();
        }
    }


    /***********************************************/
    /************** METODOS DEL BUSCADOR ***********/
    /***********************************************/

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        try {
            ArrayList<Anime> listaFiltrada = filter(listaAnimes, newText);
            adapterAnimes.setFilter(listaFiltrada);
        } catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    /**
     *  FILTRADO DEL BUSCADOR
     *  Si lo que tecleo se encuentra en la lista de animes lo agrego a una
     *  lista donde muestre los animes que coinciden con lo buscado segun el titulo
     */
    private ArrayList<Anime> filter(ArrayList<Anime> animes, String texto){
        ArrayList<Anime> listaFiltrada = new ArrayList<>();

        try {
            //Separar la palabra del buscador
            texto = texto.toLowerCase();
            for (Anime anime : animes){
                // Separa el titulo del anime
                String tituloAnime = anime.getTitulo().toLowerCase();
                // Comparar los textos separados
                if (tituloAnime.contains(texto)){
                    // Si coincide con algun titulo, lo añadre a la lista filtrada
                    listaFiltrada.add(anime);
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return listaFiltrada;
    }
}
