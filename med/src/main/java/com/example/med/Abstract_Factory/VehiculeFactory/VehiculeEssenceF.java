package com.example.med.Abstract_Factory.VehiculeFactory;

import com.example.med.Abstract_Factory.Automobile.Automobile;
import com.example.med.Abstract_Factory.Scooter.Scooter;

public class VehiculeEssenceF implements VehiculeFactory {
    public static final String TYPE_VEHICULE = "Automobile";
    public static final String TYPE_SCOOTER = "Scooter";

    @Override
    public Automobile createAutomobile() {
        return new com.example.med.Abstract_Factory.Automobile.AutomobileEssence();
    }

    @Override
    public Scooter createScooter() {
        return new com.example.med.Abstract_Factory.Scooter.ScooterEssence();
    }

    @Override
    public VehiculeElectricF commanderVehicule(String typVehicule) {
        return null;   
    }
    @Override
    public VehiculeEssenceF commanderVehiculeEssence(String typVehicule) {
        if (typVehicule.equalsIgnoreCase(TYPE_VEHICULE)) {
            createAutomobile();
        } else if (typVehicule.equalsIgnoreCase(TYPE_SCOOTER)) {
            createScooter();
        } else {
            System.out.println("Type de véhicule inconnu.");
        }
        return this;
    }
}
