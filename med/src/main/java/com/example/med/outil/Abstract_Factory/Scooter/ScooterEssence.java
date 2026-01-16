package com.example.med.outil.Abstract_Factory.Scooter;

/**
 * PATTERN ABSTRACT FACTORY - ConcreteProductB1
 *
 * Implémentation concrète d'un scooter à essence.
 * Créé uniquement par VehiculeEssenceF pour garantir la cohérence de famille.
 */
public class ScooterEssence extends Scooter {

    @Override
    public void afficherDetails() {
        System.out.println("Scooter à ESSENCE");
        System.out.println("- Type de moteur: Thermique essence");
        System.out.println("- Cylindrée: 50cc - 500cc");
        System.out.println("- Carburant: Sans plomb");
    }
}
