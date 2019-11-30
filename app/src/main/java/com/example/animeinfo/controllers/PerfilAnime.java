package com.example.animeinfo.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.animeinfo.R;
import com.example.animeinfo.model.Anime;

import org.w3c.dom.Text;

public class PerfilAnime extends AppCompatActivity {

    private Anime anime;
    private TextView titulo;
    private TextView info;
    private ImageView foto;
    private boolean fav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_anime);

        titulo = findViewById(R.id.idTituloPerfil);
        info = findViewById(R.id.idInfoPerfil);
        foto = findViewById(R.id.idImagenPerfil);

        anime = (Anime) getIntent().getSerializableExtra("anime");
        titulo.setText(anime.getTitulo());
        info.setText(anime.getInfo());
        foto.setImageResource(anime.getFoto());
        fav = anime.getFavorito();

    }

    /** Crea el menu **/
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_perfil_anime, menu);
        comprobarFavorito(menu, fav);
        return true;
    }

    /** Comprueba si el anime es favorito y cambia el icono **/
    public void comprobarFavorito(Menu menu, Boolean fav){
        if (fav)
            menu.getItem(1).setIcon(R.drawable.fav_press);
        else
            menu.getItem(1).setIcon(R.drawable.fav);
    }

    /** Metodo onClick del menu **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.guardar:
                onClickVolver();
                return true;
            case R.id.favorito:
                addFavoritos(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /** Añadir o elminar de favoritos **/
    public void addFavoritos(MenuItem item){
        if (fav){
            fav = false;
            item.setIcon(R.drawable.fav);
        } else {
            fav = true;
            item.setIcon(R.drawable.fav_press);
        }
    }


    /** Guardar favorito y Volver a la Activdad anterior **/
    public void onClickVolver() {
        anime.setFavorito(fav); // Guardar favorito si precede
        Intent intencion = new Intent(PerfilAnime.this, MainActivity.class);
        intencion.putExtra("anime", anime);
        setResult(RESULT_OK, intencion);
        finish();
    }
}
