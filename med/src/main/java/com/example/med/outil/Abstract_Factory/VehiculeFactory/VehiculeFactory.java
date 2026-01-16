package com.example.med.outil.Abstract_Factory.VehiculeFactory;

import com.example.med.outil.Abstract_Factory.Automobile.Automobile;
import com.example.med.outil.Abstract_Factory.Scooter.Scooter;

/**
 * PATTERN ABSTRACT FACTORY - AbstractFactory
 *
 * Interface définissant les méthodes de création pour une famille de véhicules.
 * Chaque factory concrète (VehiculeEssenceF, VehiculeElectricF) implémente cette
 * interface pour créer des véhicules cohérents d'une même famille.
 *
 * Structure du pattern:
 * - AbstractFactory: VehiculeFactory (cette interface)
 * - ConcreteFactory: VehiculeEssenceF, VehiculeElectricF
 * - AbstractProductA: Automobile
 * - AbstractProductB: Scooter
 * - ConcreteProducts: AutomobileEssence, AutomobileElectric, ScooterEssence, ScooterElectric
 *
 * Exemple d'utilisation:
 * VehiculeFactory factory = new VehiculeEssenceF();
 * Automobile auto = factory.createAutomobile(); // AutomobileEssence
 * Scooter scooter = factory.createScooter();    // ScooterEssence
 */
public interface VehiculeFactory {

    /**
     * Crée une automobile de la famille correspondante
     * @return Une instance d'Automobile (essence ou électrique selon la factory)
     */
    Automobile createAutomobile();

    /**
     * Crée un scooter de la famille correspondante
     * @return Une instance de Scooter (essence ou électrique selon la factory)
     */
    Scooter createScooter();
}
