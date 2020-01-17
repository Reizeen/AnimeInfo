package com.example.animeinfo.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animeinfo.R;
import com.example.animeinfo.model.Anime;

public class PerfilAnime extends AppCompatActivity {

    private Anime anime;
    private TextView titulo;
    private TextView info;
    private TextView estreno;
    private ImageView foto;
    private boolean fav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_anime);

        titulo = findViewById(R.id.idTituloPerfil);
        estreno = findViewById(R.id.idEstrenoPerfil);
        info = findViewById(R.id.idInfoPerfil);
        foto = findViewById(R.id.idImagenPerfil);

        anime = (Anime) getIntent().getSerializableExtra("anime");

        titulo.setText(anime.getTitulo());
        estreno.setText("Año de estreno: " + anime.getEstreno());
        info.setText(anime.getInfo());
        foto.setImageResource(anime.getFoto());
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
            case R.id.delete:
                eliminarAnime();
            default:
                return super.onOptionsItemSelected(item);
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
                        intencion.putExtra("operacionCode", -1);
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
        intencion.putExtra("operacionCode", 0);
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
        emailIntent.setType("text/plain");
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
