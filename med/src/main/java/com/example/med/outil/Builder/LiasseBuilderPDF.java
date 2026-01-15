package com.example.med.outil.Builder;

import com.example.med.model.commande_et_document.Document;
import com.example.med.model.commande_et_document.TypeDocument;
import com.example.med.model.commande_et_document.TypeFormat;
import com.example.med.outil.adapter.DocumentPdf;

public class LiasseBuilderPDF extends LiasseBuilder {

    @Override
    public void construireBonCommande(String titre) {
        // Utilisation de l'Adapter PDF avec l'objet Commande complet
        DocumentPdf adapter = new DocumentPdf(titre);
        adapter.preparerDonnees(this.commandeRef); 
        
        String url = adapter.imprimer(); // Génère le PDF avec tableau des prix et infos client

        Document doc = new Document();
        doc.setType(TypeDocument.BON_COMMANDE);
        doc.setFormat(TypeFormat.PDF);
        doc.setUrl(url);
        this.liasse.ajouterDocument(doc);
    }

    @Override
    public void construireCertificatCession(String titre) {
        DocumentPdf adapter = new DocumentPdf(titre);
        adapter.preparerDonnees(this.commandeRef);
        
        String url = adapter.imprimer();

        Document doc = new Document();
        doc.setType(TypeDocument.CERTIFICAT_CESSION);
        doc.setFormat(TypeFormat.PDF);
        doc.setUrl(url);
        this.liasse.ajouterDocument(doc);
    }

    @Override
    public void construireDemandeImmatriculation(String titre) {
        DocumentPdf adapter = new DocumentPdf(titre);
        adapter.preparerDonnees(this.commandeRef);
        
        String url = adapter.imprimer();

        Document doc = new Document();
        doc.setType(TypeDocument.DEMANDE_IMMATRICULATION);
        doc.setFormat(TypeFormat.PDF);
        doc.setUrl(url);
        this.liasse.ajouterDocument(doc);
    }
}
