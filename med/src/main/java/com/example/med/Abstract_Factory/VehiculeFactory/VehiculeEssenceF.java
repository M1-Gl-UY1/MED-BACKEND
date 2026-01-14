package com.example.med.Abstract_Factory.VehiculeFactory;

import com.example.med.Abstract_Factory.Automobile.Automobile;
import com.example.med.Abstract_Factory.Scooter.Scooter;
import com.example.med.Abstract_Factory.Scooter.ScooterEssence;
import com.example.med.Abstract_Factory.Automobile.AutomobileEssence;



public class VehiculeEssenceF implements VehiculeFactory {


    @Override
    public Automobile createAutomobile() {
        return new AutomobileEssence();
    }

    @Override
    public Scooter createScooter() {
        return new ScooterEssence();
    }
}
