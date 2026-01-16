package com.example.med.outil.adapter;

import com.example.med.model.commande_et_document.Document;

/**
 * PATTERN ADAPTER - Adapter (Adaptateur PDF)
 *
 * Adapte le composant PdfGenerator a l'interface DocumentExport.
 *
 * Structure:
 * - Target: DocumentExport (cette interface)
 * - Adapter: DocumentPDFExporter (cette classe)
 * - Adaptee: PdfGenerator (composant technique)
 */
public class DocumentPDFExporter implements DocumentExport {

    // Composant technique adapte (Adaptee)
    private PdfGenerator pdfGenerator = new PdfGenerator();

    /**
     * Exporte le document au format PDF en utilisant PdfGenerator
     */
    @Override
    public void exporter(Document document) {
        String url = pdfGenerator.genererPdfDocument(document);
        document.setUrl(url);
    }
}
