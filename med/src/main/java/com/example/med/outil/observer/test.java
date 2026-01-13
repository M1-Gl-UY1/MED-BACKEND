package com.example.med.outil.observer;

import com.example.med.model.catalogue.Vehicule;

public class test {
    public static void main(String[] args) {
        // Création du Sujet (Le véhicule)
        Vehicule maVoiture = new Vehicule();
        maVoiture.setNom("Tesla Model 3");
        maVoiture.setPrix(45000);

        // Création des observateurs
        VehiculeObserver affichageClient = new ConcretVehiculeObserver("Ecran Magasin");
        VehiculeObserver serviceMarketing = new ConcretVehiculeObserver("Service SMS");

        // Inscription (ajouterObserver)
        maVoiture.ajouterObserver(affichageClient);
        maVoiture.ajouterObserver(serviceMarketing);

        // Modification du prix -> Déclenche automatiquement notifier() -> update()
        System.out.println("--- Changement de prix en cours ---");
        maVoiture.setPrix(42000);

        // Sortie console :
        // [Ecran Magasin] Notification : Le prix du véhicule Tesla Model 3 a été mis à jour ! Nouveau prix : 42000.0€
        // [Service SMS] Notification : Le prix du véhicule Tesla Model 3 a été mis à jour ! Nouveau prix : 42000.0€
    }
}
