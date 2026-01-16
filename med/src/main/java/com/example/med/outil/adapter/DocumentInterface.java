package com.example.med.outil.adapter;

import com.example.med.model.commande_et_document.Commande;

/**
 * PATTERN ADAPTER - Target (Interface cible)
 *
 * Interface définissant le contrat pour l'export de documents.
 * Les adapters (DocumentPdf, DocumentHtml) implémentent cette interface
 * pour adapter les composants techniques (ComposantPdf, ComposantHtml).
 *
 * Structure du pattern:
 * - Target: DocumentInterface (cette interface)
 * - Adapter: DocumentPdf, DocumentHtml
 * - Adaptee: ComposantPdf, ComposantHtml
 *
 * Exemple d'utilisation:
 * DocumentInterface doc = new DocumentPdf("Bon de commande");
 * doc.preparerDonnees(commande);
 * String url = doc.imprimer();
 */
public interface DocumentInterface {

    /**
     * Prépare les données du document à partir de la commande
     * @param commande La commande source des données
     */
    void preparerDonnees(Commande commande);

    /**
     * Génère et imprime le document
     * @return L'URL du fichier généré
     */
    String imprimer();
}
