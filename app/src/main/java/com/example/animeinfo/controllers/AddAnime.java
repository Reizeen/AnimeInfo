package com.example.animeinfo.controllers;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.animeinfo.R;
import com.example.animeinfo.model.AnimeConstantes;
import com.example.animeinfo.model.ConexionSQLiteHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddAnime extends AppCompatActivity {

    private static final int COD_GALERIA = 10;
    private static final int COD_CAMARA = 2;

    private String currentPhotoPath;
    private Bitmap bitmapEscalado;
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
     * Evento onClick para subir una imagen desde la galeria o la camara
     */
    public void subirImagen(View view) {
        final CharSequence[] opciones = {"Tomar Foto", "Cargar Imagen", "Cancelar"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(this);
        alertOpciones.setTitle("Selecciona una opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int op) {
                if (opciones[op].equals("Tomar Foto")){
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Asegúrese de que haya una actividad de cámara para manejar la intención
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        // Crea el archivo donde debe ir la foto
                        File photoFile = null;

                        try {
                            photoFile = createImageFile();
                        } catch (IOException e) {
                            // Se produjo un error al crear el archivo
                            e.printStackTrace();
                        }

                        // Continuar solo si el archivo se creó correctamente
                        if (photoFile  != null) {
                            Uri imagenUri = FileProvider.getUriForFile(AddAnime.this, "com.example.animeinfo.fileprovider", photoFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imagenUri);
                            startActivityForResult(takePictureIntent, COD_CAMARA);
                        }
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


    /**
     * Metodo para tomar la foto
     * Crear archivo para almacenar la imagen
     * Guardar la ruta donde se almacenará la imagen
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Crear un nombre de archivo de imagen
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";

        // Guardar imagen en la ruta indicada en el xml
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".png",         /* suffix */
                storageDir      /* directory */
        );

        // Guardar un archivo: ruta para usar con ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Crear bitmap de la imagen de la galeria
     * mediante su URI
     * @param uri
     * @throws IOException
     */
    public Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap imageGallery = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return imageGallery;
    }

    /**
     * Darle tamaño a la imagen
     * @param bitmap
     * @return
     */
    public Bitmap escalarImagen(Bitmap bitmap){
        double alto = 0;
        double ancho = 0;
        double factor = 0;

        Log.i(null, "escalarImagen: ----------------------" + bitmap.getWidth() + " x " + bitmap.getHeight());
        if(bitmap.getHeight() > bitmap.getWidth()){
            factor = (double)bitmap.getHeight() / (double)bitmap.getWidth();
            alto = 400;
            ancho = alto / factor;
        } else {
            factor = (double)bitmap.getWidth() / (double)bitmap.getHeight();
            Log.i(null, "escalarImagen: ----------------------" + factor);
            ancho = 400;
            alto = ancho / factor;
        }
        Log.i(null, "escalarImagen: ----------------------" + ancho + " x " + alto);
        return Bitmap.createScaledBitmap(bitmap, (int)ancho, (int)alto, true);
    }


    /**
     * Cargar la imagen de la galeria en la actividad
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == COD_GALERIA){
            Bitmap bitmap = null;
            try {
                bitmap = getBitmapFromUri(data.getData());

            } catch (IOException e) {
                e.printStackTrace();
            }
            bitmapEscalado = escalarImagen(bitmap);
            imagen.setImageBitmap(bitmapEscalado);
        } else if (resultCode == RESULT_OK && requestCode == COD_CAMARA){
            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
            bitmapEscalado = escalarImagen(bitmap);
            imagen.setImageBitmap(bitmapEscalado);
        }
    }


    /**
     * Convertir el bitmap de la imagen a BLOB
     * Insertar el nuevo anime en la BD
     * @return
     */
    public Boolean insertarAnime(){
        byte[] blob;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(175000);

        /* Si el bitmapEscalado no se ha producido por no insertar una imagen
         * establece una imagen por defecto.
         */
        if (bitmapEscalado == null){
            Bitmap bitmp = BitmapFactory.decodeResource(getResources(), R.drawable.imagen_no_disponible);
            Bitmap bitmpEscladoDefecto = escalarImagen(bitmp);
            bitmpEscladoDefecto.compress(Bitmap.CompressFormat.PNG, 0 , baos);
        } else {
            bitmapEscalado.compress(Bitmap.CompressFormat.PNG, 0 , baos);
        }

        blob = baos.toByteArray();
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, AnimeConstantes.NOMBRE_DB, null, 1);

        // Poder escribir en la base de datod
        SQLiteDatabase db = conn.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AnimeConstantes.TITULO, titulo.getText().toString());
        values.put(AnimeConstantes.ESTRENO, estreno.getText().toString());
        values.put(AnimeConstantes.FAVORITO, 0);
        values.put(AnimeConstantes.IMAGEN, blob);
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
