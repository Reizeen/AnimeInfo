package com.example.animeinfo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.example.animeinfo.R;
import com.example.animeinfo.model.Anime;

import java.util.ArrayList;

public class AdapterAnimes extends RecyclerView.Adapter<AdapterAnimes.ViewHolderDatos>
                            implements View.OnClickListener {

    private ArrayList<Anime> listaAnimes;
    private View.OnClickListener listener;

    public AdapterAnimes(ArrayList<Anime> listaAnimes) {
        this.listaAnimes = listaAnimes;
    }

    @Override
    public ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, null, false);
        view.setOnClickListener(this);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderDatos holder, int position) {
        holder.nombreText.setText(listaAnimes.get(position).getTitulo());
        holder.infoText.setText(listaAnimes.get(position).getInfo());
        holder.fotoImage.setImageResource(listaAnimes.get(position).getFoto());
    }

    @Override
    public int getItemCount() {
        return listaAnimes.size();
    }

    /**
     * Evento de seleccion
     */
    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null)
            listener.onClick(view);
    }

    /**
     * Actuliza el Recycler si hay modificaciones
     */
    public void setFilter(ArrayList<Anime> listaAnimes){
         this.listaAnimes = new ArrayList<>();
         this.listaAnimes.addAll(listaAnimes);
         notifyDataSetChanged(); // Actualizar si hay cambios
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        TextView nombreText, infoText;
        ImageView fotoImage;

        public ViewHolderDatos(View itemView) {
            super(itemView);
            nombreText = (TextView) itemView.findViewById(R.id.idTitulo);
            infoText = (TextView) itemView.findViewById(R.id.idInfo);
            fotoImage = (ImageView) itemView.findViewById(R.id.idImagen);
        }
    }
}
