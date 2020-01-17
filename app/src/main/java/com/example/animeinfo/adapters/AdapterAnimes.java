package com.example.animeinfo.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.animeinfo.R;
import com.example.animeinfo.model.AnimeConstantes;

public class AdapterAnimes extends RecyclerView.Adapter<AdapterAnimes.ViewHolder> {

    private final Context contexto;
    private Cursor items;
    public OnItemClickListener escucha;

    public interface OnItemClickListener {
        void onClick(ViewHolder holder, int idPromocion);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nombreText, infoText;
        ImageView fotoImage;

        public ViewHolder(View view) {
            super(view);
            nombreText = itemView.findViewById(R.id.idTitulo);
            infoText = itemView.findViewById(R.id.idInfo);
            fotoImage = itemView.findViewById(R.id.idImagen);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            escucha.onClick(this, obtenerIdAnime(getAdapterPosition()));
        }
    }

    /**
     * Retorna en el valor de la columna "ID" de la posición actual.
     * Este método es muy útil a la hora de leer los eventos de click y mostrar detalles.
     * @param posicion
     * @return
     */
    private int obtenerIdAnime(int posicion) {
        if (items != null) {
            if (items.moveToPosition(posicion)) {
                return items.getInt(AnimeConstantes.COLUMN_ID);
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    public AdapterAnimes(Context contexto, OnItemClickListener escucha) {
        this.contexto = contexto;
        this.escucha = escucha;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Acceder a la posición del cursor dependiendo del parámetro position
        items.moveToPosition(position);

        // Asignación
        holder.nombreText.setText(items.getString(AnimeConstantes.COLUMN_TITULO));
        holder.infoText.setText(items.getString(AnimeConstantes.COLUMN_TITULO));
        holder.fotoImage.setImageResource(items.getInt(AnimeConstantes.COLUMN_FOTO));
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
