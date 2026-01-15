package com.example.med.outil.adapter;

import com.example.med.model.commande_et_document.Commande;

public interface DocumentInterface {
    void preparerDonnees(Commande commande);
    String imprimer();
}

