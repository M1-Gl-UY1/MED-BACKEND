package com.example.med.outil.adapter;

import com.example.med.model.commande_et_document.Commande;

public class DocumentHtml implements DocumentInterface {
    
    // Le composant technique "adapté"
    private ComposantHtml outilHtml = new ComposantHtml();
    
    private String titre;
    private Commande commande;

    public DocumentHtml(String titre) {
        this.titre = titre;
    }

    @Override
    public void preparerDonnees(Commande commande) {
        this.commande = commande;
    }

    
    @Override
    public String imprimer() {
        // On délègue le travail au composant technique spécialisé dans le HTML
        return outilHtml.htmlPrepareAffichage(this.titre, this.commande);
    }
}