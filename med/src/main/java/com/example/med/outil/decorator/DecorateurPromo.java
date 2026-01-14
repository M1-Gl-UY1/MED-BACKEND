package com.example.med.outil.decorator;

import com.example.med.model.catalogue.Vehicule;

public class DecorateurPromo extends DecorateurVehicule{
    public DecorateurPromo(Vehicule vehicule) {
        super(vehicule);
    }

    @Override
    public String getNom() {
        return vehicule.getNom()+" [PROMO -10%]";
    }

    @Override
    public double getPrix(double facteurReduction) {
        return vehicule.getPrixBase()*facteurReduction;
    }
}
