package com.example.med.outil.adapter;

import com.example.med.model.commande_et_document.Commande;

/**
 * PATTERN ADAPTER - Adapter (Adaptateur)
 *
 * Adapte le composant technique ComposantPdf à l'interface DocumentInterface.
 * Permet d'utiliser la bibliothèque PDF externe de manière uniforme.
 *
 * Structure:
 * - Implémente Target (DocumentInterface)
 * - Compose Adaptee (ComposantPdf)
 * - Traduit les appels de l'interface vers le composant adapté
 */
public class DocumentPdf implements DocumentInterface {

    // Composant technique adapté (Adaptee)
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

    /**
     * Délègue la génération au composant PDF adapté
     */
    @Override
    public String imprimer() {
        return outilPdf.pdfPrepareAffichage(titre, commande);
    }
}
