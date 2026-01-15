package com.example.med.outil.Builder;

import com.example.med.model.commande_et_document.*;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

public class LiasseBuilderHTML extends LiasseBuilder {

    @Override
    public void construireBonCommande(String contenu) {
        String url = genererFichierHTML("BonCommande", contenu);
        
        Document doc = new Document();
        doc.setType(TypeDocument.BON_COMMANDE);
        doc.setFormat(TypeFormat.HTML); // Format HTML
        doc.setUrl(url);
        this.liasse.ajouterDocument(doc);
    }

    @Override
    public void construireCertificatCession(String contenu) {
        String url = genererFichierHTML("CertificatCession", contenu);
        
        Document doc = new Document();
        doc.setType(TypeDocument.CERTIFICAT_CESSION);
        doc.setFormat(TypeFormat.HTML);
        doc.setUrl(url);
        this.liasse.ajouterDocument(doc);
    }

    @Override
    public void construireDemandeImmatriculation(String contenu) {
        String url = genererFichierHTML("DemandeImmat", contenu);
        
        Document doc = new Document();
        doc.setType(TypeDocument.DEMANDE_IMMATRICULATION);
        doc.setFormat(TypeFormat.HTML);
        doc.setUrl(url);
        this.liasse.ajouterDocument(doc);
    }

    // Petite méthode utilitaire pour créer un vrai fichier HTML
    private String genererFichierHTML(String nom, String contenu) {
        String chemin = "documents/" + nom + "_" + System.currentTimeMillis() + ".html";
        try {
            File dir = new File("documents/");
            if (!dir.exists()) dir.mkdirs();
            
            FileWriter writer = new FileWriter(chemin);
            writer.write("<html><body><h1>" + nom + "</h1><p>" + contenu + "</p></body></html>");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return chemin;
    }
}