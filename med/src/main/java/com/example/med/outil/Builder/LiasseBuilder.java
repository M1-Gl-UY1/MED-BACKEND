package com.example.med.outil.Builder;

import com.example.med.model.commande_et_document.Commande;
import com.example.med.model.commande_et_document.LiasseDocuments;

public abstract class LiasseBuilder {
    protected LiasseDocuments liasse;
     protected Commande commandeRef; 

     public void creerNouvelleLiasse(Commande commande) {
        
        this.liasse = LiasseDocuments.creerNouvelleInstance();
        this.commandeRef = commande; 
    }

    public LiasseDocuments getLiasse() {
        return liasse;
    }

    public abstract void construireBonCommande(String contenu);
    public abstract void construireCertificatCession(String contenu);
    public abstract void construireDemandeImmatriculation(String contenu);
}
