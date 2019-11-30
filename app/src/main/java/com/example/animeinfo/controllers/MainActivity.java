package com.example.animeinfo.controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    /** Crear Menu del MainActivity
      * Crear Buscador para el menu **/
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

    /** FILTRADO DEL BUSCADOR **/
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

    /** Insertar datos iniciales al RecyclerView **/
    public void llenarAnimes() {
        listaAnimes.add(new Anime(0,"Tokyo Ghoul", true, R.drawable.tokyoghoul, "En Tokio ocurren varias muertes, cometidas por Ghouls, seres desconocidos que sobreviven a base de carne humana. Un día Ken Kaneki, un joven de 18 años conoce a una chica llamada Rize Kamishiro en una cafetería y la invita a salir. Tras una cita aparentemente normal, Kaneki se ofrece a acompañarla a su casa. Al entrar a un callejón es atacado por ella, quien resulta ser un Ghoul. Durante el ataque, Kaneki es salvado por la caída de unas vigas que matan a Rize antes de que pueda matarlo, pero lo deja terriblemente herido. Debido a la gravedad de sus heridas, recibe un trasplante de órganos por el ghoul atacante (Rize Kamishiro). Así, Kaneki termina convirtiéndose en un ser mitad Ghoul y mitad Humano, de ahora en adelante deberá vivir escondiéndose de los humanos sin tener a quién recurrir. Conoce a unos Ghouls quienes trabajan en la cafetería donde conoció a Rize y se entera que los trabajadores de esa cafetería también resultan ser ghouls que sobreviven a base de cafe, ya que el cafe es el único gusto que comparten los Ghouls y los humanos. Lo demás es rechazado por el estómago de los Ghouls, salvo la carne humana, que han de consumir cada cierto tiempo.Durante el transcurso de la historia Kaneki conocerá a nuevos compañeros y verá el lado oscuro de tanto la humanidad como de los Ghouls"));
        listaAnimes.add(new Anime(1,"Ataque a los Titanes", false, R.drawable.ataque_a_los_titanes, "La historia comienza con Eren Jaeger quien vive de forma pacífica con su familia, su hermana adoptiva Mikasa Ackerman, y su único amigo Armin Arlert. Estos viven en el «distrito de Shiganshina» (シガンシナ区 Shiganshina-ku?), una de las principales ciudades, ubicadas en la muralla María. Pero todo cambia cuando un titán gigantesco crea una enorme grieta permitiendo la entrada de los titanes a la ciudad, mientras que otro titan fuertemente blindado traspasa la muralla María haciendo de su protección inútil, y provocando una evacuación masiva en toda la población hacia la muralla interna Rose, la segunda de tres murallas concéntricas. Durante el ataque, Eren observa con horror como un titán se come a su madre, mientras que su padre desaparece después de entregarle a Eren la llave del sótano de su casa, no sin antes encomendarle regresar a su hogar algún día. Después de la desaparición de su padre, Eren jura matar a todos los titanes y se alista en el ejército junto con sus amigos.\n" +
                "\n" +
                "Cinco años después, los tres jóvenes se han graduados de cadetes y viven en ese momento en el «distrito de Trost» (トロスト区 Torosuto-ku?), una de las ciudades fronterizas que sobresale de la muralla Rose. Sin embargo, apareció de nuevo el mismo titán y la historia se repite. En la batalla subsiguiente, Eren logra salvar a Armin de ser comido por un titán a costa de ser devorado. Mientras toda esperanza de sobrevivir parecía extinguirse, un titán musculoso y atípico aparece atacando a los otros titanes en lugar de a los humanos. Dicho titán se revela como Eren quien por alguna razón se ha transformado en uno, para sorpresa de todos, en especial de Mikasa y Armin. Aunque algunos, como los nobles, el clero, y la Policía militar, lo consideran una amenaza a pesar de ayudar a recuperar el Distrito de Trost, otros ven en la habilidad de Eren una oportunidad de salvar a la humanidad; por lo que después de ser sometido a un juicio militar en el que finalmente, se decidió que Eren forme parte del Cuerpo de exploración, asignándolo al Escuadrón de operaciones especiales bajo mando del capitán Levi.\n" +
                "\n" +
                "Conforme avanza la trama, se dan a conocer muchos misterios respecto a la capacidad de Eren en transformarse en titán, desde las razones de como consiguió dicho poder, el hecho de que no es el único que posee esta habilidad. También se descubrirán la procedencia y origen de los titanes, la revelación de sus verdaderos enemigos, y de las decisiones que Eren deberá tomar respecto al futuro de la humanidad, el de sus amigos, y el suyo propio. "));
    }

    /** Metodo onClick del menu **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_anadir:
                abreCrearAnime();
                return true;
            case R.id.favorito:
               // addFavoritos(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /** Abre la actividad del perfil de un item del RecyclerView */
    public void abrePerfilAnime(AdapterAnimes adapterAnimes){
        adapterAnimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Anime anime = listaAnimes.get(recyclerAnimes.getChildAdapterPosition(view));
                Intent intencion = new Intent(MainActivity.this, PerfilAnime.class);
                intencion.putExtra("anime", anime);
                startActivityForResult(intencion, 101);
            }
        });
    }

    /** Abre la actividad del perfil de un item del RecyclerView */
    public void abreCrearAnime(){
        Intent intencion = new Intent(MainActivity.this, AddAnime.class);
        intencion.putExtra("id", listaAnimes.size());
        startActivityForResult(intencion, 102);
    }

    /** Guardar informacion de las otras actividades **/
    public void onActivityResult(int requestCode, int resultCode, Intent code){
        if (requestCode == 101 && resultCode == RESULT_OK){
            Anime animeMod = (Anime) code.getSerializableExtra("anime");
            for (Anime anime : listaAnimes) {
                if (anime.getId() == animeMod.getId()){
                    anime.setFavorito(animeMod.getFavorito());
                }
            }
        }
    }
}
