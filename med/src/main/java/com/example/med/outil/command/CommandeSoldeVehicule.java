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
        vehicule.setPrixBase(ancienPrix * (1-tauxReduction));
        vehicule.setSolde(true);
    }

    @Override
    public void annuler() {
        vehicule.setPrixBase(ancienPrix);
        vehicule.setSolde(false);
    }
}
