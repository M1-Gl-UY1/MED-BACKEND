package com.example.med.outil.decorator;

import com.example.med.model.catalogue.Vehicule;

public class DecorateurDestock extends DecorateurVehicule{
    public DecorateurDestock(Vehicule vehicule) {
        super(vehicule);
    }

    @Override
    public String getNom() {
        return vehicule.getNom()+" [DESTOCKAGE]";
    }


    @Override
    public double getPrix(double facteurReduction) {
        return vehicule.getPrixBase()*facteurReduction;
    }
}
