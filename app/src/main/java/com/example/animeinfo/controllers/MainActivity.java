package com.example.animeinfo.controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.example.animeinfo.R;
import com.example.animeinfo.adapters.AdapterAnimes;
import com.example.animeinfo.model.Anime;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private AdapterAnimes adapterAnimes;
    private ArrayList<Anime> listaAnimes;
    private RecyclerView recyclerAnimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listaAnimes = new ArrayList<>();
        recyclerAnimes = findViewById(R.id.idRecyclerView);
        recyclerAnimes.setLayoutManager(new LinearLayoutManager(this));

        llenarAnimes();
        adapterAnimes = new AdapterAnimes(listaAnimes);
        recyclerAnimes.setAdapter(adapterAnimes);

        abrePerfilAnime(adapterAnimes);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_buscar);
        // Asignar un SearchView al boton del action
        SearchView buscador = (SearchView) MenuItemCompat.getActionView(item);
        // Que el buscador escuche en este contesto
        buscador.setOnQueryTextListener(this);

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

    public void llenarAnimes() {
        listaAnimes.add(new Anime("Shingeki no Kyiojin", "Extraños asesinatos se están sucediendo uno tras otro en Tokyo. Debido a las pruebas encontradas en las escenas, la policía concluye que los ataques son obra de ghouls que se comen a las personas. Kaneki y Hide, dos compañeros de clase, llegan a la conclusión de que si nadie ha visto nunca a esos necrófagos es porque toman la apariencia de seres humanos para ocultarse. Poco sabían entonces de que su teoría sería más cierta de lo que pensaban cuando Kaneki es herido de gravedad por un monstruo y comienza a atraerle cada vez más la carne humana…\n",R.drawable.tokyoghoul));
        listaAnimes.add(new Anime("One Piece", "Extraños asesinatos se están sucediendo uno tras otro en Tokyo. Debido a las pruebas encontradas en las escenas, la policía concluye que los ataques son obra de ghouls que se comen a las personas. Kaneki y Hide, dos compañeros de clase, llegan a la conclusión de que si nadie ha visto nunca a esos necrófagos es porque toman la apariencia de seres humanos para ocultarse. Poco sabían entonces de que su teoría sería más cierta de lo que pensaban cuando Kaneki es herido de gravedad por un monstruo y comienza a atraerle cada vez más la carne humana…\n",R.drawable.tokyoghoul));
        listaAnimes.add(new Anime("Alabasta", "Extraños asesinatos se están sucediendo uno tras otro en Tokyo. Debido a las pruebas encontradas en las escenas, la policía concluye que los ataques son obra de ghouls que se comen a las personas. Kaneki y Hide, dos compañeros de clase, llegan a la conclusión de que si nadie ha visto nunca a esos necrófagos es porque toman la apariencia de seres humanos para ocultarse. Poco sabían entonces de que su teoría sería más cierta de lo que pensaban cuando Kaneki es herido de gravedad por un monstruo y comienza a atraerle cada vez más la carne humana…\n",R.drawable.tokyoghoul));
        listaAnimes.add(new Anime("Zumitos", "Extraños asesinatos se están sucediendo uno tras otro en Tokyo. Debido a las pruebas encontradas en las escenas, la policía concluye que los ataques son obra de ghouls que se comen a las personas. Kaneki y Hide, dos compañeros de clase, llegan a la conclusión de que si nadie ha visto nunca a esos necrófagos es porque toman la apariencia de seres humanos para ocultarse. Poco sabían entonces de que su teoría sería más cierta de lo que pensaban cuando Kaneki es herido de gravedad por un monstruo y comienza a atraerle cada vez más la carne humana…\n",R.drawable.tokyoghoul));
        listaAnimes.add(new Anime("Tokyo Ghoul", "Extraños asesinatos se están sucediendo uno tras otro en Tokyo. Debido a las pruebas encontradas en las escenas, la policía concluye que los ataques son obra de ghouls que se comen a las personas. Kaneki y Hide, dos compañeros de clase, llegan a la conclusión de que si nadie ha visto nunca a esos necrófagos es porque toman la apariencia de seres humanos para ocultarse. Poco sabían entonces de que su teoría sería más cierta de lo que pensaban cuando Kaneki es herido de gravedad por un monstruo y comienza a atraerle cada vez más la carne humana…\n",R.drawable.tokyoghoul));
    }

    public void abrePerfilAnime(AdapterAnimes adapterAnimes){
        adapterAnimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Anime anime = listaAnimes.get(recyclerAnimes.getChildAdapterPosition(view));
                Intent intencion = new Intent(MainActivity.this, PerfilAnime.class);
                Bundle datos = new Bundle();
                datos.putSerializable("anime", anime);
                intencion.putExtras(datos);
                startActivityForResult(intencion, 101);
            }
        });
    }

    /** METODOS DEL BUSCADOR **/
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

    /** FILTRADO DE BUSCADOR **/
    /** Si lo que tecleo se encuentra en la lista de animes lo agrego a una
     * lista donde muestre los animes que coinciden con lo buscado segun el titulo **/
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
