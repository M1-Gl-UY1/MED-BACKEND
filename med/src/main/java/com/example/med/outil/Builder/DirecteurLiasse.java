package com.example.med.outil.Builder;

import com.example.med.model.commande_et_document.Commande;

public class DirecteurLiasse {
    protected LiasseBuilder builder;

    public DirecteurLiasse(LiasseBuilder builder) {
        this.builder = builder;
    }

    public void construireLiasse(Commande commande) {
        // On initialise la liasse avec la commande
        builder.creerNouvelleLiasse(commande);
        
        // On lance la fabrication des 3 documents obligatoires
        builder.construireBonCommande("Bon de Commande Officiel");
        builder.construireCertificatCession("Certificat de Cession de Véhicule");
        builder.construireDemandeImmatriculation("Demande d'Immatriculation Préfecture");
    }
}