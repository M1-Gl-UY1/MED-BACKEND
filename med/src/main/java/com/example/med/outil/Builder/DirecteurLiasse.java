package com.example.med.outil.Builder;

public class DirecteurLiasse {
    protected LiasseBuilder builder;

    public DirecteurLiasse(LiasseBuilder builder) {
        this.builder = builder;
    }

    public void construireLiasse(String nomClient, String detailsVehicule) {
        builder.creerNouvelleLiasse();
        builder.construireBonCommande("Bon pour " + nomClient + " : " + detailsVehicule);
        builder.construireCertificatCession("Cession véhicule à " + nomClient);
        builder.construireDemandeImmatriculation("Immatriculation véhicule : " + detailsVehicule);
    }
}