package com.example.med.outil.Builder;

import com.example.med.model.commande_et_document.LiasseDocuments;

public class DirecteurLiasse {
    private LiasseBuilder builder;

    public DirecteurLiasse(LiasseBuilder builder) {
        this.builder = builder;
    }

    public void construireLiasse() {
        builder.creerNouvelleLiasse();
        builder.construireBonCommande("Détails commande...");
        builder.construireCertificatCession("Détails cession...");
        builder.construireDemandeImmatriculation("Détails immatriculation...");
    }

    public LiasseDocuments getResultat() {
        return builder.getLiasse();
    }
}