package com.example.med.outil.command;

import com.example.med.model.catalogue.Vehicule;

/**
 * PATTERN COMMAND - ConcreteCommand
 *
 * Commande concrète pour solder un véhicule.
 * Encapsule l'action de réduction de prix avec possibilité d'annulation.
 *
 * Stocke l'ancien prix pour permettre l'opération undo.
 * Interagit avec le Receiver (Vehicule) pour effectuer l'action.
 */
public class CommandeSoldeVehicule implements CommandeAction {

    // Receiver - l'objet sur lequel l'action est effectuée
    private Vehicule vehicule;

    // État sauvegardé pour l'annulation (undo)
    private double ancienPrix;

    // Paramètre de la commande
    private double tauxReduction;

    /**
     * Constructeur avec le véhicule cible et le taux de réduction
     * @param vehicule Le véhicule à solder
     * @param tauxReduction Le taux de réduction (ex: 0.20 pour 20%)
     */
    public CommandeSoldeVehicule(Vehicule vehicule, double tauxReduction) {
        this.vehicule = vehicule;
        this.tauxReduction = tauxReduction;
    }

    /**
     * Exécute le solde du véhicule
     * - Sauvegarde l'ancien prix
     * - Applique la réduction
     * - Marque le véhicule comme soldé
     * - Notifie les observers via setPrix()
     */
    @Override
    public void executer() {
        ancienPrix = vehicule.getPrixBase();
        double nouveauPrix = ancienPrix * (1 - tauxReduction);
        vehicule.setPrix(nouveauPrix); // Notifie les observers
        vehicule.setSolde(true);
    }

    /**
     * Annule le solde (undo)
     * - Restaure l'ancien prix
     * - Retire le marquage soldé
     */
    @Override
    public void annuler() {
        vehicule.setPrix(ancienPrix);
        vehicule.setSolde(false);
    }
}
