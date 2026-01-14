package com.example.med.outil.factory;

import com.example.med.model.commande_et_document.Commande;
import com.example.med.model.panier.StatutPanier;

import java.time.LocalDate;

public abstract class CreateurCommande {

    public Commande creerCommande() {
        Commande commande = creer();
        commande.setDate(LocalDate.now());
        return commande;
    }

    protected abstract Commande creer();
}
