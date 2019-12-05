package com.example.animeinfo.controllers;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.animeinfo.R;
import com.example.animeinfo.model.Anime;

public class AddAnime extends AppCompatActivity {

    private ImageView imagen;
    private Anime anime;
    private EditText estreno;
    private EditText fuente;
    private EditText titulo;
    private EditText info;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_anime);

        imagen = findViewById(R.id.imagenSubida);
        id = getIntent().getIntExtra("id", -1);
        titulo = findViewById(R.id.idTituloAdd);
        estreno = findViewById(R.id.idEstrenoAdd);
        fuente = findViewById(R.id.idFuenteAdd);
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

    /**
     * Volver a la actividad con los datos insertados en el nuevo objeto
     */
    public void onVolver(View view) {
        anime = new Anime(id, titulo.getText().toString(), estreno.getText().toString(), false, R.drawable.imagen_no_disponible_dos, fuente.getText().toString(), info.getText().toString());
        Intent intencion = new Intent(AddAnime.this, MainActivity.class);
        intencion.putExtra("anime", anime);
        setResult(RESULT_OK, intencion);
        finish();
    }
}
