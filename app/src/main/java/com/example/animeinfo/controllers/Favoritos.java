package com.example.animeinfo.controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.animeinfo.R;
import com.example.animeinfo.adapters.AdapterAnimes;
import com.example.animeinfo.model.AnimeConstantes;
import com.example.animeinfo.model.ConexionSQLiteHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

        recyclerAnimes = findViewById(R.id.idRecyclerFavoritos);
        recyclerAnimes.setLayoutManager(new LinearLayoutManager(this));

        iniciarAsyncTask();
    }


    public String readJSON() {
        StringBuilder json = new StringBuilder();
        try {
            URL web = new URL("http://" + AnimeConstantes.IP + "/favoritos");
            HttpURLConnection urlConn = (HttpURLConnection) web.openConnection();
            InputStream in = new BufferedInputStream(urlConn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String inputLine;
            while ((inputLine = reader.readLine()) != null)
                json.append(inputLine);

            in.close();
            urlConn.disconnect();
        } catch(IOException e){
            e.printStackTrace();
        }

        Log.i(null, "JSON: " + json.toString());
        return json.toString();
    }

    /**
     * Cargar solo los objetos que estén en favoritos
     */
    public Cursor selectAnimes() {
        MatrixCursor cursor = new MatrixCursor(
                new String[] {"c_id", "c_titulo", "c_estreno", "c_favorito", "c_url", "c_info"});
        try {
            JSONArray respJSON = new JSONArray(readJSON());

            for(int i=0; i < respJSON.length(); i++) {
                JSONObject obj = respJSON.getJSONObject(i);

                /**convertir string a blob para meterlo en el cursor
                 * y no tener que cambiar el adaptador */
                /*String imagen = obj.getString("imagen");
                byte[] byteImage = imagen.getBytes();*/

                cursor.newRow()
                        .add("c_id", obj.getInt("id"))
                        .add("c_titulo", obj.getString("titulo"))
                        .add("c_estreno", obj.getString("estreno"))
                        .add("c_favorito", obj.getInt("favorito"))
                        .add("c_url", obj.getString("url"))
                        .add("c_info", obj.getString("info"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cursor;
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

            if(isCancelled())
                return false;

            adapterAnimes = new AdapterAnimes(getApplicationContext(), selectAnimes());

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
