package com.example.animeinfo.controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.example.animeinfo.R;
import com.example.animeinfo.adapters.AdapterAnimes;
import com.example.animeinfo.model.AnimeConstantes;
import com.example.animeinfo.model.ConexionSQLiteHelper;

public class Favoritos extends AppCompatActivity {

    private final int SEGUNDOS_ESPERA = 10;

    private ProgressDialog pd;
    private Favoritos.MiAsyncTask miAsyncTask;

    private AdapterAnimes adapterAnimes;
    private RecyclerView recyclerAnimes;
    private ConexionSQLiteHelper conexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        this.setTitle("Favoritos");

        // Conectamos a la BD
        conexion = new ConexionSQLiteHelper(this, AnimeConstantes.NOMBRE_DB, null, 1);

        recyclerAnimes = findViewById(R.id.idRecyclerFavoritos);
        recyclerAnimes.setLayoutManager(new LinearLayoutManager(this));

        iniciarAsyncTask();
    }

    /**
     * Cargar solo los objetos que estén en favoritos
     */
    public Cursor selectAnimes(String where) {
        SQLiteDatabase db = conexion.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " +
                AnimeConstantes.ID + ", " +
                AnimeConstantes.TITULO + ", " +
                AnimeConstantes.ESTRENO + ", " +
                AnimeConstantes.FAVORITO + ", " +
                AnimeConstantes.IMAGEN + ", " +
                AnimeConstantes.URL_WEB + ", " +
                AnimeConstantes.INFO_DESCRIPCION + " " +
                "FROM " + AnimeConstantes.TABLA_ANIME + where, null);

        return c;
    }

    /**
     * Mostrar un ProgressDialog para la ejecucción del AsyncTask
     */
    public void iniciarAsyncTask(){
        // Initialize a new instance of progress dialog
        pd = new ProgressDialog(Favoritos.this);

        // Set progress dialog style spinner
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        // Set the progress dialog title and message
        pd.setMessage("Cargando...");
        pd.setCancelable(true);
        pd.setMax(100);

        miAsyncTask = new Favoritos.MiAsyncTask();
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
                    Favoritos.MiAsyncTask.this.cancel(true);
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
                        break;
                    Thread.sleep(1000);
                } catch(InterruptedException e) {}

            }
            adapterAnimes = new AdapterAnimes(getApplicationContext(), selectAnimes(" WHERE " + AnimeConstantes.FAVORITO + " = 1"));

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
            if(result) {
                pd.dismiss();

                recyclerAnimes.setAdapter(adapterAnimes);
                Toast.makeText(Favoritos.this, "Datos cargados!", Toast.LENGTH_SHORT).show();
            }
        }

        /**
         * Se ejecutará cuando se cancele la ejecución de la tarea antes de su finalización normal.
         */
        @Override
        protected void onCancelled() {
            Toast.makeText(Favoritos.this, "Carga cancelada!", Toast.LENGTH_SHORT).show();
        }
    }
}
