package com.example.med.outil.Abstract_Factory.Scooter;

import com.example.med.model.catalogue.Vehicule;

/**
 * PATTERN ABSTRACT FACTORY - AbstractProductB
 *
 * Classe abstraite représentant un scooter.
 * Définit l'interface commune pour tous les scooters (essence et électrique).
 *
 * Hérite de Vehicule pour intégration avec le modèle de données existant.
 */
public abstract class Scooter extends Vehicule {

    /**
     * Affiche les détails spécifiques du scooter
     * Implémenté différemment selon le type (essence ou électrique)
     */
    public abstract void afficherDetails();
}
