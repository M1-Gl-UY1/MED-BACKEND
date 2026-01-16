package com.example.med.outil.Abstract_Factory.Automobile;

/**
 * PATTERN ABSTRACT FACTORY - ConcreteProductA1
 *
 * Implémentation concrète d'une automobile à essence.
 * Créée uniquement par VehiculeEssenceF pour garantir la cohérence de famille.
 */
public class AutomobileEssence extends Automobile {

    @Override
    public void afficherDetails() {
        System.out.println("Automobile à ESSENCE");
        System.out.println("- Type de moteur: Thermique essence");
        System.out.println("- Carburant: Sans plomb 95/98");
        System.out.println("- Émissions CO2: Standard");
    }
}
