package com.example.animeinfo.controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.animeinfo.R;
import com.example.animeinfo.adapters.AdapterAnimes;
import com.example.animeinfo.model.Anime;
import com.example.animeinfo.model.AnimeConstantes;
import com.example.animeinfo.model.ConexionSQLiteHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.security.auth.login.LoginException;

public class MainActivity extends AppCompatActivity {

    private final int COD_PERFIL = 10;
    private final int CODE_PERMISOS = 20;
    private final int COD_MODIFICAR = 101;
    private final int COD_FAVORITO = 102;
    private final int COD_ELIMINAR = 103;
    private final int COD_INSERTAR = 104;

    private Anime animeMod;

    // Cola donde se guardara los request que solicitamos.
    private RequestQueue queue;

    private AdapterAnimes adapterAnimes;
    private RecyclerView recyclerAnimes;

    private ProgressDialog pd;
    private AsyncTaskGET asyncTaskGET;
    private AsyncTaskPUT asyncTaskPUT;
    private AsyncTaskDELETE asyncTaskDELETE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        solicitarPermisos();

        queue = Volley.newRequestQueue(this);

        recyclerAnimes = findViewById(R.id.idRecyclerView);
        recyclerAnimes.setLayoutManager(new LinearLayoutManager(this));

        iniciarAsyncTaskGET();
    }


    /**
     * Solicitar permisos al usuario
     */
    public void solicitarPermisos(){
        int permisoEscritura = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permisoLectura = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permisoTelefono = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE);
        int permisoCamara = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);

        if (permisoEscritura != PackageManager.PERMISSION_GRANTED || permisoLectura != PackageManager.PERMISSION_GRANTED ||
                permisoTelefono != PackageManager.PERMISSION_GRANTED || permisoCamara!= PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CALL_PHONE, Manifest.permission.CAMERA},
                        CODE_PERMISOS);
            }
        }
    }


    /**
     * Crear Menu del MainActivity
     * Crear Buscador para el menu
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_buscar);

        // Asignar un SearchView al boton del action
        SearchView search = (SearchView) item.getActionView();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Separar la palabra del buscador
                newText = newText.toLowerCase();

                // * Si no se está  buscando nada se actualiza la lista actual
                // * sino se actualiza la lista filtrada.
                if (newText.isEmpty()){
                    iniciarAsyncTaskGET();
                } else {
                    buscadorVolleyGET(newText);
                }
                return true;
            }
        });
        return true;
    }

    /**
     * Buscador utilizando Volley con GET
     * @return
     */
    public void buscadorVolleyGET(String titulo) {
        String url = "http://" + AnimeConstantes.IP + "/animes/" + titulo;
        JsonArrayRequest requestString = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    MatrixCursor cursor = new MatrixCursor(new String[] {"c_id", "c_titulo", "c_estreno", "c_favorito", "c_url", "c_info"});

                    for(int i=0; i < response.length(); i++) {
                        JSONObject obj = response.getJSONObject(i);

                        //Toast.makeText(MainActivity.this, obj.getString("titulo"), Toast.LENGTH_SHORT).show();
                        cursor.newRow()
                                .add("c_id", obj.getInt("id"))
                                .add("c_titulo", obj.getString("titulo"))
                                .add("c_estreno", obj.getString("estreno"))
                                .add("c_favorito", obj.getInt("favorito"))
                                .add("c_url", obj.getString("url"))
                                .add("c_info", obj.getString("info"));
                    }

                    adapterAnimes.swapCursor(cursor);

                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onErrorResponse Volley:", error.toString());
            }
        });
        RequestQueue request = Volley.newRequestQueue(this);
        request.add(requestString);
    }


    /**
     * Metodo onClick del menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_anadir:
                abreCrearAnime();
                return true;
            case R.id.action_favoritos:
                abreFavoritos();
                return true;
            case R.id.action_llamar:
                llamarTienda();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Llamar por telefono a la tienda
     */
    private void llamarTienda() {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:672629147")));
    }


    /**
     * Abre la actividad de añadir un nuevo Anime
     */
    public void abreCrearAnime() {
        Intent intencion = new Intent(getApplicationContext(), AddAnime.class);
        startActivityForResult(intencion, COD_INSERTAR);
    }


    /**
     * Abre la actividad de Favoritos
     */
    private void abreFavoritos() {
        Intent intent = new Intent(MainActivity.this, Favoritos.class);
        startActivityForResult(intent, 103);
    }


    /**
     * Metodo OnClick para abrir la actividad de cada item del RecyclerView
     */
    public void abrePerfilAnime() {
        adapterAnimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Anime anime = adapterAnimes.obtenerAnime(recyclerAnimes.getChildAdapterPosition(view));
                Intent intencion = new Intent(getApplicationContext(), PerfilAnime.class);
                intencion.putExtra("anime", anime);
                startActivityForResult(intencion, COD_PERFIL);
            }
        });
    }


    /**
     * Indicar si se tiene que modificar favorito, borrar o insertar
     * un anime cuando vuelves de otra actividad.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent code) {
        if (requestCode == COD_PERFIL && resultCode == RESULT_OK) {
           animeMod = (Anime) code.getSerializableExtra("anime");

           int cod = code.getIntExtra("operacionCode", 0);
           if (cod == COD_FAVORITO){
               iniciarAsyncTaskPUT(animeMod);
           } else if (cod == COD_ELIMINAR){
               iniciarAsyncTaskDELETE(animeMod);
           } else if (cod == COD_MODIFICAR)
               iniciarAsyncTaskGET();

        } else if (requestCode == COD_INSERTAR && resultCode == RESULT_OK) {
            Toast.makeText(getApplicationContext(), "Anime insertado correctamente", Toast.LENGTH_SHORT).show();
            iniciarAsyncTaskGET();
        }


    }




    /** ==================================================================
     *  ======================= CLASES AsyncTask =========================
     *  ================================================================== */

    public String readJSON() {
         StringBuilder json = new StringBuilder();
         try {
             URL web = new URL("http://" + AnimeConstantes.IP + "/animes");
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

         Log.i(null, "-------OBSERVAR JSON------- " + json.toString());
         return json.toString();
     }

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

    public void iniciarAsyncTaskGET(){
        // Initialize a new instance of progress dialog
        pd = new ProgressDialog(MainActivity.this);

        // Set progress dialog style spinner
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        // Set the progress dialog title and message
        pd.setMessage("Cargando...");
        pd.setCancelable(false);
        pd.setMax(100);

        asyncTaskGET = new AsyncTaskGET();
        asyncTaskGET.execute();
    }

    private class AsyncTaskGET extends AsyncTask<Void, Integer, Boolean>{

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
                    AsyncTaskGET.this.cancel(true);
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

            adapterAnimes = new AdapterAnimes(getApplicationContext(), selectAnimes());
            abrePerfilAnime();

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
                recyclerAnimes.setAdapter(adapterAnimes);
            }
        }
    }


    /**
     * PUT Fvorito
     * @param animeMod
     */
    public void iniciarAsyncTaskPUT(Anime animeMod){
        // Initialize a new instance of progress dialog
        pd = new ProgressDialog(MainActivity.this);

        // Set progress dialog style spinner
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        // Set the progress dialog title and message
        pd.setMessage("Cargando...");
        pd.setCancelable(false);
        pd.setMax(100);

        asyncTaskPUT = new AsyncTaskPUT(animeMod);
        asyncTaskPUT.execute();
    }

    public int modificarFavorito(Anime animeMod) {

        int numFav = 0;
        if (animeMod.getFavorito())
            numFav = 1;

        // Actualizamos favorito en la base de datos
        try {
            URL urlWeb = new URL("http://" + AnimeConstantes.IP + "/favorito/" + animeMod.getId());
            HttpURLConnection httpConn = (HttpURLConnection) urlWeb.openConnection();

            String c_favorito = "favorito=" + numFav;

            httpConn.setRequestMethod("PUT");
            httpConn.setDoOutput(true);

            // Establecer application/x-www-form-urlencoded debido a la simplicidad de los datos
            httpConn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

            OutputStream out = new BufferedOutputStream(httpConn.getOutputStream());
            out.write(c_favorito.getBytes());
            // Obliga a escribir los datos para que no solo se queden en memoria.
            out.flush();
            out.close();

            Log.i(null, "modificarFavorito: " + httpConn.getResponseMessage());

            httpConn.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return numFav;
    }

    private class AsyncTaskPUT extends AsyncTask<Void, Integer, Boolean>{

        private Anime animeMod;
        private String mensaje;
        private final int ADD_FAVORITO = 1;
        private final int DEL_FAVORITO = 0;

        public AsyncTaskPUT(Anime animeMod){
            this.animeMod = animeMod;
        }

        @Override
        protected void onPreExecute() {
            pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    AsyncTaskPUT.this.cancel(true);
                }
            });

            pd.setProgress(0);
            pd.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            int fav = modificarFavorito(animeMod);

            if (fav == ADD_FAVORITO)
                mensaje = "Añadido a favoritos";
            else if (fav == DEL_FAVORITO)
                mensaje = "Eliminado de favoritos";

            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progreso = values[0].intValue();
            pd.setProgress(progreso);
        }

        protected void onPostExecute(Boolean result) {
            pd.dismiss();
            iniciarAsyncTaskGET();
            Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * DELETE Anime
     * @param animeMod
     */
    public void iniciarAsyncTaskDELETE(Anime animeMod){
        // Initialize a new instance of progress dialog
        pd = new ProgressDialog(MainActivity.this);

        // Set progress dialog style spinner
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        // Set the progress dialog title and message
        pd.setMessage("Eliminando...");
        pd.setCancelable(false);
        pd.setMax(100);

        asyncTaskDELETE = new AsyncTaskDELETE(animeMod);
        asyncTaskDELETE.execute();
    }

    public void eliminarAnime(Anime animeMod) {
        try {
            URL urlWeb = new URL("http://" + AnimeConstantes.IP + "/anime/" + animeMod.getId());
            HttpURLConnection httpConn = (HttpURLConnection) urlWeb.openConnection();

            httpConn.setDoOutput(true);
            httpConn.setRequestMethod("DELETE");
            httpConn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

            httpConn.connect();
            Log.i(null, "eliminarAnime: " + httpConn.getResponseMessage());
            httpConn.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class AsyncTaskDELETE extends AsyncTask<Void, Integer, Boolean>{

        private Anime animeMod;

        public AsyncTaskDELETE(Anime animeMod){
            this.animeMod = animeMod;
        }

        @Override
        protected void onPreExecute() {
            pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    AsyncTaskDELETE.this.cancel(true);
                }
            });

            pd.setProgress(0);
            pd.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            eliminarAnime(animeMod);
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progreso = values[0].intValue();
            pd.setProgress(progreso);
        }

        protected void onPostExecute(Boolean result) {
            pd.dismiss();
            iniciarAsyncTaskGET();
            Toast.makeText(MainActivity.this, "Anime eliminado correctamente", Toast.LENGTH_SHORT).show();
        }
    }





}