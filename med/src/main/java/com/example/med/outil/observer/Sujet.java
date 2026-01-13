package com.example.med.outil.observer;

public interface Sujet {
    void ajouterObserver(VehiculeObserver observer);
    void retirerObserver(VehiculeObserver observer);
    void notifier();
}
