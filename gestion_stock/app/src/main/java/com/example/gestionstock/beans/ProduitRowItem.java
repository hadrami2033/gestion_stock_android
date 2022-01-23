package com.example.gestionstock.beans;

import android.graphics.drawable.Drawable;

public class ProduitRowItem {
    private Drawable image;
    private String libelle;
    private String quantite;

    public ProduitRowItem(Drawable image, String libelle, String quantite) {
        this.image = image;
        this.libelle = libelle;
        this.quantite = quantite;
    }
    public Drawable getImage() {
        return image;
    }
    public void setImage(Drawable image) {
        this.image = image;
    }
    public String getQuantite() {
        return quantite;
    }
    public void setQuantite(String quantite) {
        this.quantite = quantite;
    }
    public String getLibelle() {
        return libelle;
    }
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
    @Override
    public String toString() {
        return libelle + "\n" + quantite + "\n" + image;
    }
}
