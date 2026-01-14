package com.example.med.outil.Abstract_Factory.VehiculeFactory;

import com.example.med.outil.Abstract_Factory.Automobile.Automobile;
import com.example.med.outil.Abstract_Factory.Automobile.AutomobileEssence;
import com.example.med.outil.Abstract_Factory.Scooter.Scooter;
import com.example.med.outil.Abstract_Factory.Scooter.ScooterEssence;



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
