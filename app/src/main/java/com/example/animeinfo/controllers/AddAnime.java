package com.example.animeinfo.controllers;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animeinfo.R;
import com.example.animeinfo.model.Anime;
import com.example.animeinfo.model.AnimeConstantes;
import com.example.animeinfo.model.ConexionSQLiteHelper;

public class AddAnime extends AppCompatActivity {

    private ImageView imagen;
    private Anime anime;
    private EditText estreno;
    private EditText url;
    private EditText titulo;
    private EditText info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_anime);

        titulo = findViewById(R.id.idTituloAdd);
        estreno = findViewById(R.id.idEstrenoAdd);
        imagen = findViewById(R.id.imagenSubida);
        url = findViewById(R.id.idFuenteAdd);
        info = findViewById(R.id.idInfoAdd);
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


    public Boolean insertarAnime(){
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, AnimeConstantes.NOMBRE_DB, null, 1);

        // Poder escribir en la base de datod
        SQLiteDatabase db = conn.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AnimeConstantes.TITULO, titulo.getText().toString());
        values.put(AnimeConstantes.ESTRENO, estreno.getText().toString());
        values.put(AnimeConstantes.FAVORITO, 0);
        values.put(AnimeConstantes.URL_WEB, url.getText().toString());
        values.put(AnimeConstantes.INFO_DESCRIPCION, info.getText().toString());

        //Insertamos el registro en la base de datos
        if(db.insert(AnimeConstantes.TABLA_ANIME, null, values) != -1)
            return true;
        return false;

    }

    /**
     * Volver a la actividad con los datos insertados en el nuevo objeto
     */
    public void onVolver(View view) {
        if (insertarAnime())
            finish();
        else
            Toast.makeText(this, "Error al insertar los datos", Toast.LENGTH_SHORT);
    }
}
