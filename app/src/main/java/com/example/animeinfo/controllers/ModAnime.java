package com.example.animeinfo.controllers;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.example.animeinfo.model.Anime;
import com.example.animeinfo.model.AnimeConstantes;
import com.example.animeinfo.model.ConexionSQLiteHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ModAnime extends AppCompatActivity {

    private static final int COD_GALERIA = 10;
    private static final int COD_CAMARA = 2;
    private final int SEGUNDOS_ESPERA = 10;

    private ProgressDialog pd;
    private MiAsyncTask miAsyncTask;

    private ConexionSQLiteHelper conexion;
    private String currentPhotoPath;
    private Bitmap bitmapEscalado;
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

        ByteArrayInputStream bais = new ByteArrayInputStream(anime.getFoto());
        Bitmap foto = BitmapFactory.decodeStream(bais);
        imagen.setImageBitmap(foto);

        url.setText(anime.getUrl());
        info.setText(anime.getInfo());
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
                            Uri imagenUri = FileProvider.getUriForFile(ModAnime.this, "com.example.animeinfo.fileprovider", photoFile);
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
     * Modificar anime
     * @return
     */
    public void modificarAnime(){
        // Convertir Bitmap en Blob
        byte[] blob;

        if (bitmapEscalado == null){
            blob = anime.getFoto();
        } else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(175000);
            bitmapEscalado.compress(Bitmap.CompressFormat.PNG, 0 , baos);
            blob = baos.toByteArray();
        }

        anime.setTitulo(titulo.getText().toString());
        anime.setEstreno(estreno.getText().toString());
        anime.setFoto(blob);
        anime.setUrl(url.getText().toString());
        anime.setInfo(info.getText().toString());

        // Poder escribir en la base de datod
        SQLiteDatabase db = conexion.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AnimeConstantes.TITULO, anime.getTitulo());
        values.put(AnimeConstantes.ESTRENO, anime.getEstreno());
        values.put(AnimeConstantes.IMAGEN, blob);
        values.put(AnimeConstantes.URL_WEB, anime.getUrl());
        values.put(AnimeConstantes.INFO_DESCRIPCION, anime.getInfo());

        //Modificamos el registro en la base de datos
        db.update(AnimeConstantes.TABLA_ANIME, values, AnimeConstantes.ID + " = " + anime.getId(), null);

    }

    /**
     * Volver a la actividad con los datos insertados en el nuevo objeto
     */
    public void onVolver(View view) {
        iniciarAsyncTask();
    }

    /** ==================================================================
     *  ==================== FUNCIONALIDAD AsyncTask =====================
     *  ================================================================== /

     /**
     * Mostrar un ProgressDialog para la ejecucción del AsyncTask
     */
    public void iniciarAsyncTask(){
        // Initialize a new instance of progress dialog
        pd = new ProgressDialog(ModAnime.this);

        // Set progress dialog style spinner
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        // Set the progress dialog title and message
        pd.setMessage("Modificando...");
        pd.setCancelable(true);
        pd.setMax(100);

        miAsyncTask = new ModAnime.MiAsyncTask();
        miAsyncTask.execute();
    }

    /**
     * Clase AsyncTask
     * Utilizado para ejecutar operaciones en segundo plano,
     * en este caso para visualizar los datos del RecyclerView
     */
    private class MiAsyncTask extends AsyncTask<Void, Integer, Boolean> {

        /**
         * Método llamado antes de iniciar el procesamiento en segundo plano.
         * Establecemos un OnCenclListener por si queremos cancelar nuestro AsyncTask
         * Mostramos el ProgressDialog
         */
        @Override
        protected void onPreExecute() {
            pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    ModAnime.MiAsyncTask.this.cancel(true);
                }
            });

            pd.setProgress(0);
            pd.show();
        }

        /**
         * En este método se define el código que se ejecutará en segundo plano.
         * Recibe como parámetros los declarados al llamar al método execute(Params).
         * Ejecutará un bucle con un slepp tantas veces como items tenga el adaptador.
         * Cuanto más items más tardará en cargar.
         * Si cancelamos, terminará el bucle.
         * @param voids
         * @return
         */
        @Override
        protected Boolean doInBackground(Void... voids) {
            for (int i = 0; i < SEGUNDOS_ESPERA; i++){
                try {
                    if(isCancelled())
                        return false;
                    Thread.sleep(1000);
                    publishProgress(i * SEGUNDOS_ESPERA);
                } catch(InterruptedException e) {}
            }

            modificarAnime();

            return true;
        }

        /**
         * Este método es llamado por publishProgress(), dentro de doInBackground(Params)
         * El uso es  para actualizar el porcentaje de ProgressDialog.
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            int progreso = values[0].intValue();
            pd.setProgress(progreso);
        }

        /**
         * Este método es llamado tras finalizar doInBackground(Params).
         * Recibe como parámetro el resultado devuelto por doInBackground(Params).
         * @param result
         */
        @Override
        protected void onPostExecute(Boolean result) {
            pd.dismiss();
            if(result) {
                Intent intent = new Intent(getApplicationContext(), PerfilAnime.class);
                intent.putExtra("anime", anime);
                setResult(RESULT_OK, intent);
                finish();
            }
        }

        /**
         * Se ejecutará cuando se cancele la ejecución de la tarea antes de su finalización normal.
         */
        @Override
        protected void onCancelled() {
            Toast.makeText(ModAnime.this, "Modificación cancelada!", Toast.LENGTH_SHORT).show();
        }
    }
}
