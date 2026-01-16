package com.example.med.outil.Abstract_Factory.VehiculeFactory;

import com.example.med.outil.Abstract_Factory.Automobile.Automobile;
import com.example.med.outil.Abstract_Factory.Automobile.AutomobileEssence;
import com.example.med.outil.Abstract_Factory.Scooter.Scooter;
import com.example.med.outil.Abstract_Factory.Scooter.ScooterEssence;

/**
 * PATTERN ABSTRACT FACTORY - ConcreteFactory
 *
 * Factory concrète pour créer des véhicules à ESSENCE.
 * Tous les véhicules créés par cette factory appartiennent à la famille "Essence".
 *
 * Garantit la cohérence: une AutomobileEssence et un ScooterEssence
 * sont toujours créés ensemble.
 */
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
