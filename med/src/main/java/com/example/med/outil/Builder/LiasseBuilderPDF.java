package com.example.med.outil.Builder;

import com.example.med.model.commande_et_document.Document;
import com.example.med.model.commande_et_document.TypeDocument;
import com.example.med.model.commande_et_document.TypeFormat;
import com.example.med.outil.adapter.DocumentPdf;

public class LiasseBuilderPDF extends LiasseBuilder {

    @Override
    public void construireBonCommande(String contenu) {
        DocumentPdf adapter = new DocumentPdf("BonCommande_" + System.currentTimeMillis());
        adapter.setContenu(contenu);
        String url = adapter.imprimer(); 

        Document doc = new Document();
        doc.setType(TypeDocument.BON_COMMANDE);
        doc.setFormat(TypeFormat.PDF);
        doc.setUrl(url);
        liasse.ajouterDocument(doc);
    }

    @Override
    public void construireCertificatCession(String contenu) {
        DocumentPdf adapter = new DocumentPdf("CertificatCession_" + System.currentTimeMillis());
        adapter.setContenu(contenu);
        String url = adapter.imprimer();

        Document doc = new Document();
        doc.setType(TypeDocument.CERTIFICAT_CESSION);
        doc.setFormat(TypeFormat.PDF);
        doc.setUrl(url);
        liasse.ajouterDocument(doc);
    }

    @Override
    public void construireDemandeImmatriculation(String contenu) {
        DocumentPdf adapter = new DocumentPdf("Immat_" + System.currentTimeMillis());
        adapter.setContenu(contenu);
        String url = adapter.imprimer();

        Document doc = new Document();
        doc.setType(TypeDocument.DEMANDE_IMMATRICULATION);
        doc.setFormat(TypeFormat.PDF);
        doc.setUrl(url);
        liasse.ajouterDocument(doc);
    }
}
