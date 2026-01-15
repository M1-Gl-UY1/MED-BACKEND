package com.example.med.outil.Builder;

import com.example.med.model.commande_et_document.Document;
import com.example.med.model.commande_et_document.TypeDocument;
import com.example.med.model.commande_et_document.TypeFormat;
import com.example.med.outil.adapter.DocumentHtml;

public class LiasseBuilderHTML extends LiasseBuilder {

    @Override
    public void construireBonCommande(String titre) {
        DocumentHtml adapter = new DocumentHtml(titre);
        adapter.preparerDonnees(this.commandeRef); // Utilise la commande stockée dans le parent
        
        String url = adapter.imprimer(); 

        Document doc = new Document();
        doc.setType(TypeDocument.BON_COMMANDE);
        doc.setFormat(TypeFormat.HTML);
        doc.setUrl(url);
        this.liasse.ajouterDocument(doc);
    }

    @Override
    public void construireCertificatCession(String titre) {
        DocumentHtml adapter = new DocumentHtml(titre);
        adapter.preparerDonnees(this.commandeRef);
        String url = adapter.imprimer();

        Document doc = new Document();
        doc.setType(TypeDocument.CERTIFICAT_CESSION);
        doc.setFormat(TypeFormat.HTML);
        doc.setUrl(url);
        this.liasse.ajouterDocument(doc);
    }

    @Override
    public void construireDemandeImmatriculation(String titre) {
        DocumentHtml adapter = new DocumentHtml(titre);
        adapter.preparerDonnees(this.commandeRef);
        String url = adapter.imprimer();

        Document doc = new Document();
        doc.setType(TypeDocument.DEMANDE_IMMATRICULATION);
        doc.setFormat(TypeFormat.HTML);
        doc.setUrl(url);
        this.liasse.ajouterDocument(doc);
    }
}