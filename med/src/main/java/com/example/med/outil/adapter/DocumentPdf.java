package com.example.med.outil.adapter;

import com.example.med.model.commande_et_document.Commande;

/**
 * PATTERN ADAPTER - Adapter (Adaptateur)
 *
 * Adapte le composant technique PdfGenerator a l'interface DocumentInterface.
 * Permet d'utiliser la generation PDF de maniere uniforme.
 *
 * Structure:
 * - Target: DocumentInterface (cette interface)
 * - Adapter: DocumentPdf (cette classe)
 * - Adaptee: PdfGenerator (composant technique)
 */
public class DocumentPdf implements DocumentInterface {

    // Composant technique adapte (Adaptee)
    private PdfGenerator pdfGenerator = new PdfGenerator();
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
     * Delegue la generation au composant PDF adapte
     */
    @Override
    public String imprimer() {
        return pdfGenerator.genererPdfCommande(titre, commande);
    }
}
