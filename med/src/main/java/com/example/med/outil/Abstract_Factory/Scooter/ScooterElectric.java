package com.example.med.outil.Abstract_Factory.Scooter;

/**
 * PATTERN ABSTRACT FACTORY - ConcreteProductB2
 *
 * Implémentation concrète d'un scooter électrique.
 * Créé uniquement par VehiculeElectricF pour garantir la cohérence de famille.
 */
public class ScooterElectric extends Scooter {

    @Override
    public void afficherDetails() {
        System.out.println("Scooter ÉLECTRIQUE");
        System.out.println("- Type de moteur: Électrique");
        System.out.println("- Autonomie: 50-150 km selon modèle");
        System.out.println("- Recharge: Batterie amovible ou fixe");
    }
}
