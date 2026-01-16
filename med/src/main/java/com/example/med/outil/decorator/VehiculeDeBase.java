package com.example.med.outil.decorator;

import com.example.med.model.catalogue.Vehicule;

/**
 * PATTERN DECORATOR - ConcreteComponent
 *
 * Implémentation de base de VehiculeComposant.
 * Cette classe wrappe l'entité Vehicule et permet son utilisation
 * dans la chaîne de décoration.
 *
 * C'est le point d'entrée pour décorer un véhicule existant.
 */
public class VehiculeDeBase implements VehiculeComposant {

    private final Vehicule vehicule;

    public VehiculeDeBase(Vehicule vehicule) {
        if (vehicule == null) {
            throw new IllegalArgumentException("Le véhicule ne peut pas être null");
        }
        this.vehicule = vehicule;
    }

    @Override
    public String getNom() {
        return vehicule.getNom();
    }

    @Override
    public double getPrix() {
        return vehicule.getPrixBase();
    }

    @Override
    public String getDescription() {
        return String.format("%s %s %s (%d) - %s",
                vehicule.getMarque(),
                vehicule.getModel(),
                vehicule.getNom(),
                vehicule.getAnnee(),
                vehicule.getEngine());
    }

    @Override
    public void afficher() {
        System.out.println("=== Véhicule ===");
        System.out.println("Nom: " + getNom());
        System.out.println("Description: " + getDescription());
        System.out.println("Prix: " + getPrix() + " €");
    }

    /**
     * Permet d'accéder au véhicule sous-jacent si nécessaire
     */
    public Vehicule getVehiculeEntity() {
        return vehicule;
    }
}
