package com.example.med.outil.Iterator;

import com.example.med.model.catalogue.Vehicule;

import java.util.ArrayList;
import java.util.List;

/**
 * PATTERN ITERATOR - ConcreteAggregate
 *
 * Implémentation concrète du catalogue de véhicules.
 * Stocke les véhicules et fournit un itérateur pour les parcourir.
 *
 * La structure interne (ArrayList) est cachée aux clients
 * qui utilisent uniquement l'itérateur pour accéder aux véhicules.
 */
public class CatalogueVehicule implements Catalogue {

    private List<Vehicule> vehicules = new ArrayList<>();

    @Override
    public void addVehicule(Vehicule vehicule) {
        vehicules.add(vehicule);
    }

    @Override
    public VehiculeIterator getIterator() {
        return new CatalogueIterator(this.vehicules);
    }

    /**
     * Retourne le nombre de véhicules dans le catalogue
     * @return Le nombre de véhicules
     */
    public int size() {
        return vehicules.size();
    }
}
