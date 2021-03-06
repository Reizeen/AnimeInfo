package com.example.animeinfo.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animeinfo.R;
import com.example.animeinfo.model.Anime;

import java.io.ByteArrayInputStream;

public class PerfilAnime extends AppCompatActivity {

    private final int COD_MODIFICAR = 101;
    private final int COD_FAVORITO = 102;
    private final int COD_ELIMINAR = 103;

    private Anime anime;
    private TextView titulo;
    private TextView info;
    private TextView estreno;
    private ImageView imagen;
    private boolean fav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_anime);

        titulo = findViewById(R.id.idTituloPerfil);
        estreno = findViewById(R.id.idEstrenoPerfil);
        info = findViewById(R.id.idInfoPerfil);
        imagen = findViewById(R.id.idImagenPerfil);

        anime = (Anime) getIntent().getSerializableExtra("anime");
        cargarDatosAnime();
    }

    /**
     * Cargar datos del anime
     */
    public void cargarDatosAnime(){
        titulo.setText(anime.getTitulo());
        estreno.setText("Año de estreno: " + anime.getEstreno());
        info.setText(anime.getInfo());

        /*ByteArrayInputStream bais = new ByteArrayInputStream(anime.getFoto());
        Bitmap foto = BitmapFactory.decodeStream(bais);
        imagen.setImageBitmap(foto);*/

        imagen.setImageResource(R.drawable.imagen_no_disponible);
        fav = anime.getFavorito();
    }

    /**
     * Crea el menu
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_perfil_anime, menu);
        comprobarFavorito(menu, fav);
        return true;
    }

    /**
     * Comprueba si el anime es favorito y cambia el icono
     */
    public void comprobarFavorito(Menu menu, Boolean fav) {
        if (fav)
            menu.getItem(0).setIcon(R.drawable.fav_press);
        else
            menu.getItem(0).setIcon(R.drawable.fav);
    }

    /**
     * Metodo onClick del menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.infoWeb:
                verWebInfo();
                return true;
            case R.id.favorito:
                addFavoritos(item);
                return true;
            case R.id.compartir:
                compartirInfo();
                return true;
            case R.id.email:
                enviarCorreo();
                return true;
            case R.id.modificar:
                modificarAnime();
                return true;
            case R.id.delete:
                eliminarAnime();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Modificar Anime
     */
    public void modificarAnime(){
        Intent intent = new Intent(getApplicationContext(), ModAnime.class);
        intent.putExtra("anime", anime);
        startActivityForResult(intent, COD_MODIFICAR);
    }

    /**
     * Cargat datos del anime después de ser modificado
     * @param requestCode
     * @param resultCode
     * @param code
     */
    public void onActivityResult(int requestCode, int resultCode, Intent code) {
        if (requestCode == COD_MODIFICAR && resultCode == RESULT_OK) {
            Anime animeMod = (Anime) code.getSerializableExtra("anime");
            anime = animeMod;
            cargarDatosAnime();

            Intent intencion = new Intent(PerfilAnime.this, MainActivity.class);
            intencion.putExtra("anime", anime);
            intencion.putExtra("operacionCode", COD_MODIFICAR);
            setResult(RESULT_OK, intencion);
        }
    }

    /**
     * Eliminar Anime
     */
    private void eliminarAnime() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Eliminar");
        builder.setMessage("¿Quieres eliminar el Anime?");
        builder.setCancelable(false);
        builder.setPositiveButton("SI",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intencion = new Intent(PerfilAnime.this, MainActivity.class);
                        intencion.putExtra("anime", anime);
                        intencion.putExtra("operacionCode", COD_ELIMINAR);
                        setResult(RESULT_OK, intencion);
                        finish();
                    }
                });
        builder.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        builder.show();
    }

    /**
     * Ir a la pagina de la fuente
     */
    private void verWebInfo() {
        String enlace = anime.getUrl();
        if (!enlace.startsWith("https://") && !enlace.startsWith("http://"))
            enlace = "http://" + enlace;

        Uri uri = Uri.parse(enlace);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    /**
     * Añadir o elminar de favoritos
     */
    public void addFavoritos(MenuItem item) {
        if (fav) {
            fav = false;
            item.setIcon(R.drawable.fav);
        } else {
            fav = true;
            item.setIcon(R.drawable.fav_press);
        }
        anime.setFavorito(fav); // Guardar favorito si precede
        Intent intencion = new Intent(PerfilAnime.this, MainActivity.class);
        intencion.putExtra("anime", anime);
        intencion.putExtra("operacionCode", COD_FAVORITO);
        setResult(RESULT_OK, intencion);
    }

    /**
     * Comaprtir descripcion
     */
    private void compartirInfo() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, anime.getInfo());
        startActivity(Intent.createChooser(intent, "Compartir en"));
    }

    /**
     * Enviar correo a traves de un intent especifico para ello.
     */
    private void enviarCorreo() {
        String[] TO = {""};
        String[] CC = {""};

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "INFO ANIME");
        emailIntent.putExtra(Intent.EXTRA_TEXT, anime.getInfo());

        try {
            startActivity(Intent.createChooser(emailIntent, "Enviar mail..."));

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(PerfilAnime.this, "No hay un cliente de correo electrónico instalado.", Toast.LENGTH_SHORT).show();
        }
    }

}
