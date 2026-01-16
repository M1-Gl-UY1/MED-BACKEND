package com.example.med.outil.adapter;

import com.example.med.model.commande_et_document.Document;

/**
 * PATTERN ADAPTER - Adapter (Adaptateur HTML)
 *
 * Adapte le composant HtmlGenerator a l'interface DocumentExport.
 *
 * Structure:
 * - Target: DocumentExport (cette interface)
 * - Adapter: HTMLExporter (cette classe)
 * - Adaptee: HtmlGenerator (composant technique)
 */
public class HTMLExporter implements DocumentExport {

    // Composant technique adapte (Adaptee)
    private HtmlGenerator htmlGenerator = new HtmlGenerator();

    /**
     * Exporte le document au format HTML en utilisant HtmlGenerator
     */
    @Override
    public void exporter(Document document) {
        String url = htmlGenerator.genererHtmlDocument(document);
        document.setUrl(url);
    }
}
