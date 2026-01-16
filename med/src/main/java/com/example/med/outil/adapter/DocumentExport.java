package com.example.med.outil.adapter;

import com.example.med.model.commande_et_document.Document;

/**
 * PATTERN ADAPTER - Target (Interface cible)
 *
 * Interface définissant le contrat pour l'export de documents.
 * Conforme au schéma UML du projet.
 *
 * Structure du pattern:
 * - Target: DocumentExport (cette interface)
 * - Adapter: HTMLExporter, DocumentPDFExporter
 * - Adaptee: PDFLIBRARY
 */
public interface DocumentExport {

    /**
     * Exporte un document dans le format approprié
     * @param document Le document à exporter
     */
    void exporter(Document document);
}
