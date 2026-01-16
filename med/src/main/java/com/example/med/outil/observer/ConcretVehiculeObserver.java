package com.example.med.outil.observer;

import com.example.med.model.catalogue.Vehicule;

/**
 * PATTERN OBSERVER - ConcreteObserver
 *
 * Observateur concret qui réagit aux changements de prix des véhicules.
 * Chaque observateur a un nom pour identification dans les notifications.
 *
 * Exemple d'utilisation:
 * VehiculeObserver obs = new ConcretVehiculeObserver("Alerte Prix");
 * vehicule.ajouterObserver(obs);
 */
public class ConcretVehiculeObserver implements VehiculeObserver {

    private String nomObservateur;

    /**
     * Constructeur avec nom d'identification
     * @param nom Le nom de cet observateur
     */
    public ConcretVehiculeObserver(String nom) {
        this.nomObservateur = nom;
    }

    /**
     * Réaction au changement d'état du véhicule
     * Affiche une notification avec le nouveau prix
     */
    @Override
    public void update(Vehicule v) {
        System.out.println("[" + nomObservateur + "] Notification : Le prix du véhicule '"
                + v.getNom() + "' a été mis à jour ! Nouveau prix : " + v.getPrixBase() + " €");
    }

    public String getNomObservateur() {
        return nomObservateur;
    }

    public void setNomObservateur(String nomObservateur) {
        this.nomObservateur = nomObservateur;
    }
}
