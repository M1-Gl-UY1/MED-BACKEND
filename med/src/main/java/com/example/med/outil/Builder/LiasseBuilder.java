package com.example.med.outil.Builder;

import com.example.med.model.commande_et_document.LiasseDocuments;

public abstract class LiasseBuilder {
    protected LiasseDocuments liasse;

    public void creerNouvelleLiasse() {
        liasse = new LiasseDocuments();
    }

    public LiasseDocuments getLiasse() {
        return liasse;
    }

    public abstract void construireBonCommande(String contenu);
    public abstract void construireCertificatCession(String contenu);
    public abstract void construireDemandeImmatriculation(String contenu);
}
