package com.example.animeinfo.model;

import java.io.Serializable;

public class Anime implements Serializable {

    private int id;
    private String titulo;
    private String info;
    private int foto;
    private boolean favorito;

    public Anime(int id, String titulo,  boolean favorito, int foto, String info) {
        this.id = id;
        this.titulo = titulo;
        this.favorito = favorito;
        this.foto = foto;
        this.info = info;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean getFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }
}
