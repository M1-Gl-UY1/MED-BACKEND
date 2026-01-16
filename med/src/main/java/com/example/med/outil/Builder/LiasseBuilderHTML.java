package com.example.med.outil.Builder;

import com.example.med.model.commande_et_document.Document;
import com.example.med.model.commande_et_document.TypeDocument;
import com.example.med.model.commande_et_document.TypeFormat;
import com.example.med.outil.adapter.DocumentExport;
import com.example.med.outil.adapter.HTMLExporter;

/**
 * PATTERN BUILDER - ConcreteBuilder
 *
 * Builder concret pour créer une liasse de documents au format HTML.
 * Utilise l'Adapter HTMLExporter (conforme au schéma UML).
 */
public class LiasseBuilderHTML extends LiasseBuilder {

    // Utilisation de l'interface Target du pattern Adapter (conforme au schéma)
    private DocumentExport exporter = new HTMLExporter();

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
        doc.setFormat(TypeFormat.HTML);
        return doc;
    }
}
