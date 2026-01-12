package com.example.med.Abstract_Factory.VehiculeFactory;

import com.example.med.Abstract_Factory.Automobile.Automobile;
import com.example.med.Abstract_Factory.Scooter.Scooter;

import lombok.val;

public interface VehiculeFactory {
    public Automobile createAutomobile();
    public Scooter createScooter();

    public VehiculeElectricF commanderVehicule(String typVehicule) ;
    public VehiculeEssenceF commanderVehiculeEssence(String typVehicule) ;
}
