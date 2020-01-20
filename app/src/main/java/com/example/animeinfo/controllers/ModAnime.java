package com.example.animeinfo.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.animeinfo.R;
import com.example.animeinfo.model.Anime;
import com.example.animeinfo.model.AnimeConstantes;
import com.example.animeinfo.model.ConexionSQLiteHelper;

public class ModAnime extends AppCompatActivity {

    private ConexionSQLiteHelper conexion;
    private Anime anime;
    private ImageView imagen;
    private EditText estreno;
    private EditText url;
    private EditText titulo;
    private EditText info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod_anime);

        conexion = new ConexionSQLiteHelper(this, AnimeConstantes.NOMBRE_DB, null, 1);
        this.setTitle("Modificar Anime");

        titulo = findViewById(R.id.idTituloMod);
        estreno = findViewById(R.id.idEstrenoMod);
        imagen = findViewById(R.id.imagenSubidaMod);
        url = findViewById(R.id.idFuenteMod);
        info = findViewById(R.id.idInfoMod);

        // Cargar los datos del anime
        anime = (Anime) getIntent().getSerializableExtra("anime");
        cargarDatosAnime();
    }

    /**
     * Cargar datos del anime
     */
    public void cargarDatosAnime(){
        titulo.setText(anime.getTitulo());
        estreno.setText(anime.getEstreno());
        Uri uri = Uri.parse(anime.getFoto());
        imagen.setImageURI(uri);
        url.setText(anime.getUrl());
        info.setText(anime.getInfo());
    }

    /**
     * Coger una imagen de la galeria
     */
    public void subirImagen(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent, "Seleccione la aplicacion"), 10);
    }

    /**
     * Cargar la imagen de la galeria en la actividad
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 10){
            Uri path = data.getData();
            imagen.setImageURI(path);
        }
    }

    /**
     * Modificar anime
     * @return
     */
    public Boolean modificarAnime(){
        anime.setTitulo(titulo.getText().toString());
        anime.setEstreno(estreno.getText().toString());
        anime.setUrl(url.getText().toString());
        anime.setInfo(info.getText().toString());

        // Poder escribir en la base de datod
        SQLiteDatabase db = conexion.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AnimeConstantes.TITULO, anime.getTitulo());
        values.put(AnimeConstantes.ESTRENO, anime.getEstreno());
        values.put(AnimeConstantes.URL_WEB, anime.getUrl());
        values.put(AnimeConstantes.INFO_DESCRIPCION, anime.getInfo());

        //Insertamos el registro en la base de datos
        if(db.update(AnimeConstantes.TABLA_ANIME, values, AnimeConstantes.ID + " = " + anime.getId(), null) != -1)
            return true;
        return false;

    }

    /**
     * Volver a la actividad con los datos insertados en el nuevo objeto
     */
    public void onVolver(View view) {
        Intent intent = new Intent(getApplicationContext(), PerfilAnime.class);
        if (modificarAnime()){
            intent.putExtra("anime", anime);
            setResult(RESULT_OK, intent);
            finish();
        } else
            Toast.makeText(this, "Error al modificar los datos", Toast.LENGTH_SHORT);
    }


}
