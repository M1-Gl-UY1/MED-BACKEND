package com.example.med.outil.Abstract_Factory.Automobile;

/**
 * PATTERN ABSTRACT FACTORY - ConcreteProductA2
 *
 * Implémentation concrète d'une automobile électrique.
 * Créée uniquement par VehiculeElectricF pour garantir la cohérence de famille.
 */
public class AutomobileElectric extends Automobile {

    @Override
    public void afficherDetails() {
        System.out.println("Automobile ÉLECTRIQUE");
        System.out.println("- Type de moteur: Électrique");
        System.out.println("- Autonomie: Variable selon modèle");
        System.out.println("- Émissions CO2: Zéro émission directe");
    }
}
