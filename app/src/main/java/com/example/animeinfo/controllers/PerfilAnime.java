package com.example.animeinfo.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_anime);

        titulo = findViewById(R.id.idTituloPerfil);
        info = findViewById(R.id.idInfoPerfil);

        Bundle datos = this.getIntent().getExtras();
        anime = (Anime) datos.getSerializable("anime");
        titulo.setText("Titulo: " + anime.getTitulo());
        info.setText(anime.getInfo());
        //foto.setImageResource(anime.getFoto());;

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_perfil_anime, menu);
        return true;
    }
}
