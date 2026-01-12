package com.example.med.Abstract_Factory.VehiculeFactory;

import com.example.med.Abstract_Factory.Automobile.Automobile;
import com.example.med.Abstract_Factory.Automobile.AutomobileElectric;
import com.example.med.Abstract_Factory.Scooter.Scooter;
import com.example.med.Abstract_Factory.Scooter.ScooterElectric;

public class VehiculeElectricF implements VehiculeFactory {
    public static final String TYPE_VEHICULE = "Automobile";
    public static final String TYPE_SCOOTER = "Scooter";

    @Override
    public Automobile createAutomobile() {
        return new AutomobileElectric();
    }

    @Override
    public Scooter createScooter() {
        return new ScooterElectric();
    }

    @Override
    public VehiculeElectricF commanderVehicule(String typVehicule) {
        if (typVehicule.equalsIgnoreCase(TYPE_VEHICULE)) {
            createAutomobile();
        } else if (typVehicule.equalsIgnoreCase(TYPE_SCOOTER)) {
            createScooter();
        } else {
            System.out.println("Type de véhicule inconnu.");
        }
        return this;
    }
    @Override
    public VehiculeEssenceF commanderVehiculeEssence(String typVehicule) {
        return null;   
    }
}