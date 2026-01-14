package com.example.med.outil.decorator;

import com.example.med.model.catalogue.Vehicule;

public abstract class DecorateurVehicule implements VehiculeComposant{
    protected Vehicule vehicule;

    public DecorateurVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }
}
