package com.example.med.outil.command;

/**
 * PATTERN COMMAND - Command (Interface)
 *
 * Interface définissant le contrat pour toutes les commandes.
 * Chaque commande encapsule une action avec la possibilité de l'annuler.
 *
 * Structure du pattern:
 * - Command: CommandeAction (cette interface)
 * - ConcreteCommand: CommandeSoldeVehicule
 * - Invoker: GestionnairesCommandes
 * - Receiver: Vehicule (l'objet sur lequel l'action est effectuée)
 *
 * Exemple d'utilisation:
 * CommandeAction cmd = new CommandeSoldeVehicule(vehicule, 0.20);
 * gestionnaire.executerCommande(cmd);
 * gestionnaire.annulerDerniereCommande(); // undo
 */
public interface CommandeAction {

    /**
     * Exécute la commande
     */
    void executer();

    /**
     * Annule la commande (undo)
     */
    void annuler();
}
