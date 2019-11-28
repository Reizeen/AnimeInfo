package com.example.animeinfo.model;

import java.io.Serializable;

public class Anime implements Serializable {

    private String titulo;
    private String info;
    private int foto;

    public Anime(){}

    public Anime(String titulo, String info, int foto) {
        this.titulo = titulo;
        this.info = info;
        this.foto = foto;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }
}
