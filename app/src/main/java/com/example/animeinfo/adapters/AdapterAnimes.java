package com.example.animeinfo.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.animeinfo.R;
import com.example.animeinfo.model.Anime;
import com.example.animeinfo.model.AnimeConstantes;

public class AdapterAnimes
        extends RecyclerView.Adapter<AdapterAnimes.ViewHolder>
        implements View.OnClickListener {

    private final Context contexto;
    private Cursor items;
    private View.OnClickListener escucha;

    /**
     * Inicializa con el contexto de la actividad y
     * con el cursor de la busqueda de la bd
     * @param contexto
     * @param items
     */
    public AdapterAnimes(Context contexto, Cursor items) {
        this.contexto = contexto;
        this.items = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreText, infoText;
        ImageView fotoImage;

        public ViewHolder(View view) {
            super(view);
            nombreText = itemView.findViewById(R.id.idTitulo);
            infoText = itemView.findViewById(R.id.idInfo);
            fotoImage = itemView.findViewById(R.id.idImagen);
        }
    }

    @Override
    public void onClick(View view) {
        if (escucha != null)
            escucha.onClick(view);
    }

    /**
     * Retorna un objeto anime según en la posicion que estés en el cursor
     * @param posicion
     * @return
     */
    public Anime obtenerAnime(int posicion) {
        if (items != null) {
            if (items.moveToPosition(posicion)) {
                int id = items.getInt(AnimeConstantes.COLUMN_ID);
                String titulo = items.getString(AnimeConstantes.COLUMN_TITULO);
                String estreno = items.getString(AnimeConstantes.COLUMN_ESTRENO);
                int numFav = items.getInt(AnimeConstantes.COLUMN_FAVORITO);

                boolean fav;
                if (numFav == 1)
                    fav = true;
                else
                    fav = false;

                String foto = items.getString(AnimeConstantes.COLUMN_IMAGEN);
                String url = items.getString(AnimeConstantes.COLUMN_URL);
                String info = items.getString(AnimeConstantes.COLUMN_DESCRIPCION);

                Anime anime = new Anime(id, titulo, fav, estreno, foto, url, info);
                return anime;

            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Adapata la informacion de un objeto con la vista del card view.
     * Utiliza el metodo setOnClickListener para llevarte al perfil
     * cuando pulsas en el item del recycler view
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }


    /**
     * Se encarga de meter los datos en la vista
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Acceder a la posición del cursor dependiendo del parámetro position
        items.moveToPosition(position);

        // Asignación
        holder.nombreText.setText(items.getString(AnimeConstantes.COLUMN_TITULO));
        holder.infoText.setText(items.getString(AnimeConstantes.COLUMN_TITULO));
        Uri uri = Uri.parse(items.getString(AnimeConstantes.COLUMN_IMAGEN));
        holder.fotoImage.setImageURI(uri);
    }


    public void setOnClickListener(View.OnClickListener escucha) {
        this.escucha = escucha;
    }

    /**
     * Obtener la cantidad de ítems con el método getCount() del cursor.
     * @return
     */
    @Override
    public int getItemCount() {
        if (items != null)
            return items.getCount();
        return 0;
    }

    /**
     * Intercambia el cursor actual por uno nuevo.
     * @param nuevoCursor
     */
    public void swapCursor(Cursor nuevoCursor) {
        if (nuevoCursor != null) {
            items = nuevoCursor;
            notifyDataSetChanged();
        }
    }

    /**
     * Retorna en el cursor actual para darle uso externo.
     * @return
     */
    public Cursor getCursor() {
        return items;
    }
}