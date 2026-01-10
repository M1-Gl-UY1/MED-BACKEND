package com.example.med.model.commande_et_document;

public enum PaysLivraison {
    CM("Cameroun"),
    FR("France"),
    US("États-Unis"),
    NG("Nigeria");

    private final String libelle;

    PaysLivraison(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
