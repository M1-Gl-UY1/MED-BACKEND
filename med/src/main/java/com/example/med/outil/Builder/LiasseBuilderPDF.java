package com.example.med.outil.Builder;

import com.example.med.model.commande_et_document.Document;
import com.example.med.model.commande_et_document.TypeDocument;
import com.example.med.model.commande_et_document.TypeFormat;

public class LiasseBuilderPDF extends LiasseBuilder {
    @Override
    public void construireBonCommande(String contenu) {
        Document doc = new Document();
        doc.setType(TypeDocument.BON_COMMANDE);
        doc.setFormat(TypeFormat.PDF);
        doc.setUrl("url/pdf/bon_" + System.currentTimeMillis());
        //liasse.ajouterDocument(doc);
    }

    @Override
    public void construireCertificatCession(String contenu) {
        // Logique similaire pour certificat
    }

    @Override
    public void construireDemandeImmatriculation(String contenu) {
        // Logique similaire pour immatriculation
    }
}
