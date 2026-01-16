package com.example.med.outil.adapter;

import com.example.med.model.commande_et_document.Commande;

/**
 * PATTERN ADAPTER - Adapter (Adaptateur)
 *
 * Adapte le composant technique HtmlGenerator a l'interface DocumentInterface.
 * Permet d'utiliser la generation HTML de maniere uniforme.
 *
 * Structure:
 * - Target: DocumentInterface (cette interface)
 * - Adapter: DocumentHtml (cette classe)
 * - Adaptee: HtmlGenerator (composant technique)
 */
public class DocumentHtml implements DocumentInterface {

    // Composant technique adapte (Adaptee)
    private HtmlGenerator htmlGenerator = new HtmlGenerator();
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
     * Delegue la generation au composant HTML adapte
     */
    @Override
    public String imprimer() {
        return htmlGenerator.genererHtmlCommande(this.titre, this.commande);
    }
}
