package com.example.animeinfo.controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
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
     * Insertar datos iniciales al RecyclerView
     */
    public void llenarAnimes() {
        listaAnimes.add(new Anime(0,"Tokyo Ghoul", "2014", true, R.drawable.tokyoghoul, "https://es.wikipedia.org/wiki/Tokyo_Ghoul", "En Tokio ocurren varias muertes, cometidas por Ghouls, seres desconocidos que sobreviven a base de carne humana. Un día Ken Kaneki, un joven de 18 años conoce a una chica llamada Rize Kamishiro en una cafetería y la invita a salir. Tras una cita aparentemente normal, Kaneki se ofrece a acompañarla a su casa. Al entrar a un callejón es atacado por ella, quien resulta ser un Ghoul. Durante el ataque, Kaneki es salvado por la caída de unas vigas que matan a Rize antes de que pueda matarlo, pero lo deja terriblemente herido. Debido a la gravedad de sus heridas, recibe un trasplante de órganos por el ghoul atacante (Rize Kamishiro). Así, Kaneki termina convirtiéndose en un ser mitad Ghoul y mitad Humano, de ahora en adelante deberá vivir escondiéndose de los humanos sin tener a quién recurrir. Conoce a unos Ghouls quienes trabajan en la cafetería donde conoció a Rize y se entera que los trabajadores de esa cafetería también resultan ser ghouls que sobreviven a base de cafe, ya que el cafe es el único gusto que comparten los Ghouls y los humanos. Lo demás es rechazado por el estómago de los Ghouls, salvo la carne humana, que han de consumir cada cierto tiempo.Durante el transcurso de la historia Kaneki conocerá a nuevos compañeros y verá el lado oscuro de tanto la humanidad como de los Ghouls"));

        listaAnimes.add(new Anime(1,"Ataque a los Titanes", "2013", false, R.drawable.ataque_a_los_titanes, "https://es.wikipedia.org/wiki/Shingeki_no_Kyojin", "La historia comienza con Eren Jaeger quien vive de forma pacífica con su familia, su hermana adoptiva Mikasa Ackerman, y su único amigo Armin Arlert. Estos viven en el «distrito de Shiganshina» (シガンシナ区 Shiganshina-ku?), una de las principales ciudades, ubicadas en la muralla María. Pero todo cambia cuando un titán gigantesco crea una enorme grieta permitiendo la entrada de los titanes a la ciudad, mientras que otro titan fuertemente blindado traspasa la muralla María haciendo de su protección inútil, y provocando una evacuación masiva en toda la población hacia la muralla interna Rose, la segunda de tres murallas concéntricas. Durante el ataque, Eren observa con horror como un titán se come a su madre, mientras que su padre desaparece después de entregarle a Eren la llave del sótano de su casa, no sin antes encomendarle regresar a su hogar algún día. Después de la desaparición de su padre, Eren jura matar a todos los titanes y se alista en el ejército junto con sus amigos.\n" +
                "\n" +
                "Cinco años después, los tres jóvenes se han graduados de cadetes y viven en ese momento en el «distrito de Trost» (トロスト区 Torosuto-ku?), una de las ciudades fronterizas que sobresale de la muralla Rose. Sin embargo, apareció de nuevo el mismo titán y la historia se repite. En la batalla subsiguiente, Eren logra salvar a Armin de ser comido por un titán a costa de ser devorado. Mientras toda esperanza de sobrevivir parecía extinguirse, un titán musculoso y atípico aparece atacando a los otros titanes en lugar de a los humanos. Dicho titán se revela como Eren quien por alguna razón se ha transformado en uno, para sorpresa de todos, en especial de Mikasa y Armin. Aunque algunos, como los nobles, el clero, y la Policía militar, lo consideran una amenaza a pesar de ayudar a recuperar el Distrito de Trost, otros ven en la habilidad de Eren una oportunidad de salvar a la humanidad; por lo que después de ser sometido a un juicio militar en el que finalmente, se decidió que Eren forme parte del Cuerpo de exploración, asignándolo al Escuadrón de operaciones especiales bajo mando del capitán Levi.\n" +
                "\n" +
                "Conforme avanza la trama, se dan a conocer muchos misterios respecto a la capacidad de Eren en transformarse en titán, desde las razones de como consiguió dicho poder, el hecho de que no es el único que posee esta habilidad. También se descubrirán la procedencia y origen de los titanes, la revelación de sus verdaderos enemigos, y de las decisiones que Eren deberá tomar respecto al futuro de la humanidad, el de sus amigos, y el suyo propio. "));

        listaAnimes.add(new Anime(2, "One Piece", "1999",false, R.drawable.one_piece, "https://es.wikipedia.org/wiki/One_Piece", "La serie trata sobre Monkey D. Luffy, quien en su niñez obtuvo poderes elásticos al comer una Akuma no mi (Fruta del Diablo ). Inspirado por su amigo, el pirata Shanks, comienza un viaje para lograr su sueño: ser el “Rey de los Piratas”. Para lograrlo debe atravesar el Grand Line, un mar peligroso y desconocido donde Gol D. Roger, el difunto rey de los piratas, escondió el One Piece, un tesoro de proporciones inimaginables y que ha sido por décadas la aspiración de los más poderosos y peligrosos piratas del mundo. Sin embargo, ninguno ha logrado obtener, hasta el momento.\n" +
                "\n" +
                "Para cumplir con su objetivo, Luffy emprende un viaje por mar donde conocerá a personas excepcionales con las que formará su tripulación y enfrentará un viaje largo y peligroso en el que continuamente son puestos a prueba, enfrentándose a crueles enemigos con habilidades sobrenaturales a los que Mugiwara no Luffy (Luffy Sombrero de paja) tendrá que vencer y así acercarse a su meta de obtener el One Piece y adjudicarse el título de Kaizoku Oni (Rey de los piratas)."));

        listaAnimes.add(new Anime(3, "Inuyasha", "200f", false, R.drawable.inuyasha, "https://es.wikipedia.org/wiki/InuYasha", "La historia comienza en el Japón antiguo, donde existían los demonios y los humanos, en este mundo los demonios eran enemigos de los humanos, aunque siempre se encontraban excepción como es el caso de los padres de Inuyasha. Inuyasha es un hanyou (mitad humano y mitad demonio). \n" +
                "Inuyasha busca la legendaria gema shikon ko dama para poder convertirse en un demonio completo y asi dejar de ser despreciado por los hombres y por los demonios, durante su búsqueda del shikon no dama se encuentra con Kikiou, la poderosa y bella sacerdotisa de un pueblo, la cual tenía bajo su custodia la gema de shikon no dama. \n" +
                "Inuyasha intentara quitarle la gema muchas veces sin ningún éxito, hasta que Kikiou lo llama y le expresa que ella siente un especial afecto por él y le sugiere que se convierta en humano en vez de demonio y viva con ella, Inuyasha queda sorprendido ante tal oferta y no sabe que decir, a esto kikiou le ofrece traer la gema para que se convierta en human.\n" +
                "Adelante a esto, un personaje extraño interviene y causa un mal entendido entre ambos, el cual termina en que Kikiou queda herida mortalmente e Inuyasha queda sellado en un sueño etern. Antes de morir kikiou pide ser cremada junto con la shikon no dama para que esta última no caiga en malas manos... \n" +
                "Pasaron los años hasta la actualidad, en el mismo lugar donde se ubicaba ese antiguo pueblo se construye un templo que esta resguardado por un viejo anciano que tiene una nieta llamada Kagome. Kagome es una estudiante de preparatoria bastante común y corriente, un día antes de su cumpleaños su abuelo le regala algunas cosas extrañas, entre ellas una mano seca de kappa (un legendario monstruo o duende de agua, a Kagome realmente no le parece interesante este regalo y se lo ofrece a su gato...). \n" +
                "Luego Kagome ve un pequeño llavero con una esfera que tenía la inscripción de shikon no dama mientras que lo veía su abuelo le contaba la leyenda respecto a ella (historia que a Kagome la parecía aburrida...\n"));
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
     * Abre la actividad del perfil de un item del RecyclerView
     */
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

    /**
     * Abre la actividad del perfil de un item del RecyclerView
     */
    public void abreCrearAnime(){
        Intent intencion = new Intent(MainActivity.this, AddAnime.class);
        intencion.putExtra("id", listaAnimes.size());
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
     * Llamar por telefono a la tienda
     */
    private void llamarTienda() {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:672629147")));
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
            Anime animeAdd = (Anime) code.getSerializableExtra("anime");
            listaAnimes.add(animeAdd);
            adapterAnimes.notifyDataSetChanged();
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
