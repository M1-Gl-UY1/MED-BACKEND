package com.example.med.outil.adapter;

import com.example.med.model.commande_et_document.Document;

import java.io.File;

/**
 * PATTERN ADAPTER - Adapter (Adaptateur PDF)
 *
 * Adapte la bibliothèque PDFLIBRARY à l'interface DocumentExport.
 * Conforme au schéma UML du projet.
 *
 * Structure:
 * - Implémente Target (DocumentExport)
 * - Utilise Adaptee (PDFLIBRARY)
 */
public class DocumentPDFExporter implements DocumentExport {

    // Composant externe adapté (Adaptee)
    private PDFLIBRARY outilPDF = new PDFLIBRARY();

    /**
     * Exporte le document au format PDF en utilisant PDFLIBRARY
     */
    @Override
    public void exporter(Document document) {
        // Construire le contenu du document
        StringBuilder content = new StringBuilder();
        content.append("# ").append(document.getType().name()).append("\n\n");
        content.append("Type de document: ").append(document.getType()).append("\n");
        content.append("Format: ").append(document.getFormat()).append("\n");

        if (document.getLiasse() != null) {
            content.append("Liasse ID: ").append(document.getLiasse().getIdLiasse()).append("\n");
        }

        // Générer le chemin du fichier
        String folderPath = "documents";
        File folder = new File(folderPath);
        if (!folder.exists()) folder.mkdirs();

        String fileName = document.getType().name() + "_" + System.currentTimeMillis() + ".pdf";
        String path = folderPath + "/" + fileName;

        // Déléguer à PDFLIBRARY
        outilPDF.generatePDF(content.toString(), path);

        // Mettre à jour l'URL du document
        document.setUrl("/documents/" + fileName);
    }
}
