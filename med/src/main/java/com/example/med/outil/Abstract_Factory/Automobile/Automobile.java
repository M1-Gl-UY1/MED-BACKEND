package com.example.med.outil.Abstract_Factory.Automobile;

import com.example.med.model.catalogue.Vehicule;

/**
 * PATTERN ABSTRACT FACTORY - AbstractProductA
 *
 * Classe abstraite représentant une automobile.
 * Définit l'interface commune pour toutes les automobiles (essence et électrique).
 *
 * Hérite de Vehicule pour intégration avec le modèle de données existant.
 */
public abstract class Automobile extends Vehicule {

    /**
     * Affiche les détails spécifiques de l'automobile
     * Implémenté différemment selon le type (essence ou électrique)
     */
    public abstract void afficherDetails();
}
