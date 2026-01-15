package com.example.med.outil.command;

import com.example.med.model.catalogue.Vehicule;

public class CommandeSoldeVehicule implements CommandeAction {

    private Vehicule vehicule;
    private double ancienPrix;
    private double tauxReduction;

    public CommandeSoldeVehicule(Vehicule vehicule, double tauxReduction) {
        this.vehicule = vehicule;
        this.tauxReduction = tauxReduction;
    }

    @Override
    public void executer() {
        ancienPrix = vehicule.getPrixBase();
        double nouveauPrix = ancienPrix * (1-tauxReduction);
        vehicule.setPrix(nouveauPrix); //observer notifie
        vehicule.setSolde(true);
    }

    @Override
    public void annuler() {
        vehicule.setPrix(ancienPrix);
        vehicule.setSolde(false);
    }
}
