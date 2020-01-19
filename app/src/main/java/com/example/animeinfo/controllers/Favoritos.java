package com.example.animeinfo.controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.animeinfo.R;
import com.example.animeinfo.adapters.AdapterAnimes;
import com.example.animeinfo.model.Anime;

import java.util.ArrayList;

public class Favoritos extends AppCompatActivity {

    private AdapterAnimes adapterAnimes;
    private ArrayList<Anime> listaAnimes;
    private RecyclerView recyclerAnimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        this.setTitle("Favoritos");

        listaAnimes = (ArrayList<Anime>) getIntent().getSerializableExtra("listaAnime");
        recyclerAnimes = findViewById(R.id.idRecyclerFavoritos);
        recyclerAnimes.setLayoutManager(new LinearLayoutManager(this));

        adapterAnimes = new AdapterAnimes(animesFavoritos(listaAnimes));
        recyclerAnimes.setAdapter(adapterAnimes);
    }

    /**
     * Visualizar solo los objetos que tengan en favoritos = true
     */
    public ArrayList<Anime> animesFavoritos(ArrayList<Anime> lista){
        ArrayList<Anime> nuevaLista = new ArrayList<>();
        for (Anime anime : lista) {
            if (anime.getFavorito())
                nuevaLista.add(anime);
        }
        return nuevaLista;
    }
}
