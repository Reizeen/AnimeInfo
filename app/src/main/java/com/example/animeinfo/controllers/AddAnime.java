package com.example.animeinfo.controllers;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddAnime extends AppCompatActivity {

    private final int COD_GALERIA = 10;
    private final int COD_CAMARA = 20;

    private String absolutePathFoto;
    private ImageView imagen;
    private EditText estreno;
    private EditText url;
    private EditText titulo;
    private EditText info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_anime);

        this.setTitle("Nuevo Anime");

        titulo = findViewById(R.id.idTituloAdd);
        estreno = findViewById(R.id.idEstrenoAdd);
        imagen = findViewById(R.id.imagenSubidaAdd);
        url = findViewById(R.id.idFuenteAdd);
        info = findViewById(R.id.idInfoAdd);
    }

    /**
     * Coger una imagen de la galeria
     */
    public void subirImagen(View view) {

        final CharSequence[] opciones = {"Tomar Foto", "Cargar Imagen", "Cancelar"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(this);
        alertOpciones.setTitle("Selecciona una opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int op) {
                if (opciones[op].equals("Tomar Foto")){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File imagenCamara = null;
                    try {
                        imagenCamara = tomarFoto();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (imagenCamara != null){
                        Uri imagenUri = FileProvider.getUriForFile(AddAnime.this, "com.example.animeinfo", imagenCamara);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imagenUri);
                        startActivityForResult(intent, COD_CAMARA);
                    }

                } else if (opciones[op].equals("Cargar Imagen")){
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/");
                    startActivityForResult(intent.createChooser(intent, "Seleccione la aplicacion"), COD_GALERIA);
                } else {
                    dialog.dismiss();
                }
            }
        });
        alertOpciones.show();
    }


    private File tomarFoto() throws IOException {
        String timeFoto = new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date());
        String nameFoto = "imagenAnimeInfo_" + timeFoto;

        File storageFoto = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File fileFoto = File.createTempFile(nameFoto, ".jpg", storageFoto);
        absolutePathFoto = fileFoto.getAbsolutePath();
        return fileFoto;
    }

    /**
     * Cargar la imagen de la galeria en la actividad
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == COD_GALERIA){
            Uri path = data.getData();
            imagen.setImageURI(path);
        } else if (resultCode == RESULT_OK && requestCode == COD_CAMARA){
            imagen.setImageURI(data.getData());
        }
    }


    /**
     * Insertar el nuevo anime en la BD
     * @return
     */
    public Boolean insertarAnime(){
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, AnimeConstantes.NOMBRE_DB, null, 1);

        // Poder escribir en la base de datod
        SQLiteDatabase db = conn.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AnimeConstantes.TITULO, titulo.getText().toString());
        values.put(AnimeConstantes.ESTRENO, estreno.getText().toString());
        values.put(AnimeConstantes.FAVORITO, 0);
        values.put(AnimeConstantes.IMAGEN,  Uri.parse("android.resource://com.example.animeinfo/" + R.drawable.imagen_no_disponible_dos).toString());
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
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        if (insertarAnime()) {
            setResult(RESULT_OK, intent);
            finish();
        } else
            Toast.makeText(this, "Error al insertar los datos", Toast.LENGTH_SHORT);

    }
}
