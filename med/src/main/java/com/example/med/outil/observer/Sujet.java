package com.example.med.outil.observer;

/**
 * PATTERN OBSERVER - Subject (Sujet)
 *
 * Interface définissant les méthodes de gestion des observateurs.
 * Les classes qui implémentent cette interface (ex: Vehicule)
 * peuvent notifier les observateurs de leurs changements d'état.
 *
 * Structure du pattern:
 * - Subject: Sujet (cette interface)
 * - ConcreteSubject: Vehicule
 * - Observer: VehiculeObserver
 * - ConcreteObserver: ConcretVehiculeObserver
 *
 * Exemple d'utilisation:
 * Vehicule v = new Vehicule();
 * v.ajouterObserver(new ConcretVehiculeObserver("Client1"));
 * v.setPrix(25000); // Notifie automatiquement les observateurs
 */
public interface Sujet {

    /**
     * Ajoute un observateur à la liste
     * @param observer L'observateur à ajouter
     */
    void ajouterObserver(VehiculeObserver observer);

    /**
     * Retire un observateur de la liste
     * @param observer L'observateur à retirer
     */
    void retirerObserver(VehiculeObserver observer);

    /**
     * Notifie tous les observateurs d'un changement d'état
     */
    void notifier();
}
