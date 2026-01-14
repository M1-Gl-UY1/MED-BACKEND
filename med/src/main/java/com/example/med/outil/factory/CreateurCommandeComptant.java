package com.example.med.outil.factory;

import com.example.med.model.commande_et_document.Commande;

public class CreateurCommandeComptant extends CreateurCommande {

    @Override
    protected Commande creer() {
        return new CommandeComptant();
    }
}
