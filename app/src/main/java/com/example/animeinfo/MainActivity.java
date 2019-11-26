package com.example.animeinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;

import com.example.animeinfo.adapters.AdapterAnimes;
import com.example.animeinfo.model.Anime;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Anime> listaAnimes;
    RecyclerView recyclerAnimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listaAnimes = new ArrayList<>();
        recyclerAnimes = findViewById(R.id.idRecyclerView);
        recyclerAnimes.setLayoutManager(new LinearLayoutManager(this));

        llenarAnimes();
        AdapterAnimes adapterAnimes = new AdapterAnimes(listaAnimes);
        recyclerAnimes.setAdapter(adapterAnimes);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void llenarAnimes() {
        listaAnimes.add(new Anime("Tokyo Ghoul", "Tokyo Ghoul (東京喰種, Tōkyō Gūru, Tōkyō Kushu?) es un manga creado por Sui Ishida. Serializada en la revista de Shueisha; \"Weekly Young Jump\", con entrega semanal desde septiembre del 2011. Compilado en 14 volúmenes (tankōbon) a partir de junio del 2014. ",R.drawable.tokyoghoul));
        listaAnimes.add(new Anime("Tokyo Ghoul", "Tokyo Ghoul (東京喰種, Tōkyō Gūru, Tōkyō Kushu?) es un manga creado por Sui Ishida. Serializada en la revista de Shueisha; \"Weekly Young Jump\", con entrega semanal desde septiembre del 2011. Compilado en 14 volúmenes (tankōbon) a partir de junio del 2014. ",R.drawable.tokyoghoul));
        listaAnimes.add(new Anime("Tokyo Ghoul", "Tokyo Ghoul (東京喰種, Tōkyō Gūru, Tōkyō Kushu?) es un manga creado por Sui Ishida. Serializada en la revista de Shueisha; \"Weekly Young Jump\", con entrega semanal desde septiembre del 2011. Compilado en 14 volúmenes (tankōbon) a partir de junio del 2014. ",R.drawable.tokyoghoul));
        listaAnimes.add(new Anime("Tokyo Ghoul", "Tokyo Ghoul (東京喰種, Tōkyō Gūru, Tōkyō Kushu?) es un manga creado por Sui Ishida. Serializada en la revista de Shueisha; \"Weekly Young Jump\", con entrega semanal desde septiembre del 2011. Compilado en 14 volúmenes (tankōbon) a partir de junio del 2014. ",R.drawable.tokyoghoul));
        listaAnimes.add(new Anime("Tokyo Ghoul", "Tokyo Ghoul (東京喰種, Tōkyō Gūru, Tōkyō Kushu?) es un manga creado por Sui Ishida. Serializada en la revista de Shueisha; \"Weekly Young Jump\", con entrega semanal desde septiembre del 2011. Compilado en 14 volúmenes (tankōbon) a partir de junio del 2014. ",R.drawable.tokyoghoul));
        listaAnimes.add(new Anime("Tokyo Ghoul", "Tokyo Ghoul (東京喰種, Tōkyō Gūru, Tōkyō Kushu?) es un manga creado por Sui Ishida. Serializada en la revista de Shueisha; \"Weekly Young Jump\", con entrega semanal desde septiembre del 2011. Compilado en 14 volúmenes (tankōbon) a partir de junio del 2014. ",R.drawable.tokyoghoul));
        listaAnimes.add(new Anime("Tokyo Ghoul", "Tokyo Ghoul (東京喰種, Tōkyō Gūru, Tōkyō Kushu?) es un manga creado por Sui Ishida. Serializada en la revista de Shueisha; \"Weekly Young Jump\", con entrega semanal desde septiembre del 2011. Compilado en 14 volúmenes (tankōbon) a partir de junio del 2014. ",R.drawable.tokyoghoul));
        listaAnimes.add(new Anime("Tokyo Ghoul", "Tokyo Ghoul (東京喰種, Tōkyō Gūru, Tōkyō Kushu?) es un manga creado por Sui Ishida. Serializada en la revista de Shueisha; \"Weekly Young Jump\", con entrega semanal desde septiembre del 2011. Compilado en 14 volúmenes (tankōbon) a partir de junio del 2014. ",R.drawable.tokyoghoul));
        listaAnimes.add(new Anime("Tokyo Ghoul", "Tokyo Ghoul (東京喰種, Tōkyō Gūru, Tōkyō Kushu?) es un manga creado por Sui Ishida. Serializada en la revista de Shueisha; \"Weekly Young Jump\", con entrega semanal desde septiembre del 2011. Compilado en 14 volúmenes (tankōbon) a partir de junio del 2014. ",R.drawable.tokyoghoul));

    }
}
