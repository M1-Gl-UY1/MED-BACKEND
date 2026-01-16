package com.example.med.outil.observer;

import com.example.med.model.catalogue.Vehicule;

/**
 * PATTERN OBSERVER - Observer (Interface)
 *
 * Interface définissant le contrat pour les observateurs de véhicules.
 * Les observateurs concrets implémentent cette interface pour
 * réagir aux changements d'état des véhicules.
 */
public interface VehiculeObserver {

    /**
     * Appelé quand l'état du véhicule observé change
     * @param vehicule Le véhicule qui a changé
     */
    void update(Vehicule vehicule);
}
