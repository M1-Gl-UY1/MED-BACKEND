package com.example.med.outil.decorator;

/**
 * PATTERN DECORATOR - Component (Interface)
 *
 * Définit l'interface commune pour les objets qui peuvent avoir
 * des responsabilités ajoutées dynamiquement.
 *
 * Structure canonique du Decorator:
 * - Component (cette interface)
 * - ConcreteComponent (VehiculeDeBase)
 * - Decorator (DecorateurVehicule)
 * - ConcreteDecorator (DecorateurPromo, DecorateurDestock)
 */
public interface VehiculeComposant {

    /**
     * Retourne le nom du véhicule (peut être décoré)
     */
    String getNom();

    /**
     * Retourne le prix du véhicule (peut être décoré)
     */
    double getPrix();

    /**
     * Retourne la description du véhicule (peut être décorée)
     */
    String getDescription();

    /**
     * Affiche les détails du véhicule
     */
    void afficher();
}
