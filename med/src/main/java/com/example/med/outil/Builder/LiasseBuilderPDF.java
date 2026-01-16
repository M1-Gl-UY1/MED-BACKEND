package com.example.med.outil.Builder;

import com.example.med.model.commande_et_document.Document;
import com.example.med.model.commande_et_document.TypeDocument;
import com.example.med.model.commande_et_document.TypeFormat;
import com.example.med.outil.adapter.DocumentExport;
import com.example.med.outil.adapter.DocumentPDFExporter;

/**
 * PATTERN BUILDER - ConcreteBuilder
 *
 * Builder concret pour créer une liasse de documents au format PDF.
 * Utilise l'Adapter DocumentPDFExporter (conforme au schéma UML).
 */
public class LiasseBuilderPDF extends LiasseBuilder {

    // Utilisation de l'interface Target du pattern Adapter (conforme au schéma)
    private DocumentExport exporter = new DocumentPDFExporter();

    @Override
    public void construireBonCommande(String titre) {
        Document doc = creerDocument();
        doc.setType(TypeDocument.BON_COMMANDE);
        exporter.exporter(doc);
        this.liasse.ajouterDocument(doc);
    }

    @Override
    public void construireCertificatCession(String titre) {
        Document doc = creerDocument();
        doc.setType(TypeDocument.CERTIFICAT_CESSION);
        exporter.exporter(doc);
        this.liasse.ajouterDocument(doc);
    }

    @Override
    public void construireDemandeImmatriculation(String titre) {
        Document doc = creerDocument();
        doc.setType(TypeDocument.DEMANDE_IMMATRICULATION);
        exporter.exporter(doc);
        this.liasse.ajouterDocument(doc);
    }

    @Override
    public Document creerDocument() {
        Document doc = new Document();
        doc.setFormat(TypeFormat.PDF);
        return doc;
    }
}
