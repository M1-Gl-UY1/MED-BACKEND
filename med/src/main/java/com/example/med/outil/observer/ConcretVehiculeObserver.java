package com.example.med.outil.observer;

import com.example.med.model.catalogue.Vehicule;

public class ConcretVehiculeObserver implements VehiculeObserver {
    private String nomObservateur;

    public ConcretVehiculeObserver(String nom) {
        this.nomObservateur = nom;
    }

    @Override
    public void update(Vehicule v) {
        System.out.println("[" + nomObservateur + "] Notification : Le prix du véhicule "
                + v.getNom() + " a été mis à jour ! Nouveau prix : " + v.getPrixBase() + "€");
    }
}
