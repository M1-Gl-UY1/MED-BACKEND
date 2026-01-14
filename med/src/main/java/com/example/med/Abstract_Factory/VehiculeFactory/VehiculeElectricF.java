package com.example.med.Abstract_Factory.VehiculeFactory;

import com.example.med.Abstract_Factory.Automobile.Automobile;
import com.example.med.Abstract_Factory.Automobile.AutomobileElectric;
import com.example.med.Abstract_Factory.Scooter.Scooter;
import com.example.med.Abstract_Factory.Scooter.ScooterElectric;

public class VehiculeElectricF implements VehiculeFactory {

    @Override
    public Automobile createAutomobile() {
        return new AutomobileElectric();
    }

    @Override
    public Scooter createScooter() {
        return new ScooterElectric();
    }

}