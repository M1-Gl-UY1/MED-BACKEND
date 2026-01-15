package com.example.med.outil.adapter;

import com.example.med.model.commande_et_document.Commande;

public class DocumentPdf implements DocumentInterface {
    private ComposantPdf outilPdf = new ComposantPdf();
    private String titre;
    private Commande commande;

    public DocumentPdf(String titre) {
        this.titre = titre;
    }

    @Override
    public void preparerDonnees(Commande commande) {
        this.commande = commande;
    }

    @Override
    public String imprimer() {
        return outilPdf.pdfPrepareAffichage(titre, commande);
    }
}