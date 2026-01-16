package com.example.med.outil.adapter;

import com.example.med.model.commande_et_document.Commande;

/**
 * PATTERN ADAPTER - Adapter (Adaptateur)
 *
 * Adapte le composant technique ComposantHtml à l'interface DocumentInterface.
 * Permet d'utiliser la génération HTML de manière uniforme.
 *
 * Structure:
 * - Implémente Target (DocumentInterface)
 * - Compose Adaptee (ComposantHtml)
 * - Traduit les appels de l'interface vers le composant adapté
 */
public class DocumentHtml implements DocumentInterface {

    // Composant technique adapté (Adaptee)
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

    /**
     * Délègue la génération au composant HTML adapté
     */
    @Override
    public String imprimer() {
        return outilHtml.htmlPrepareAffichage(this.titre, this.commande);
    }
}
