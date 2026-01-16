package com.example.med.outil.Abstract_Factory.VehiculeFactory;

import com.example.med.outil.Abstract_Factory.Automobile.Automobile;
import com.example.med.outil.Abstract_Factory.Automobile.AutomobileElectric;
import com.example.med.outil.Abstract_Factory.Scooter.Scooter;
import com.example.med.outil.Abstract_Factory.Scooter.ScooterElectric;

/**
 * PATTERN ABSTRACT FACTORY - ConcreteFactory
 *
 * Factory concrète pour créer des véhicules ÉLECTRIQUES.
 * Tous les véhicules créés par cette factory appartiennent à la famille "Électrique".
 *
 * Garantit la cohérence: une AutomobileElectric et un ScooterElectric
 * sont toujours créés ensemble.
 */
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
