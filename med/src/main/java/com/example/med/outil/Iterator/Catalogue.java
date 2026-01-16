package com.example.med.outil.Iterator;

import com.example.med.model.catalogue.Vehicule;

/**
 * PATTERN ITERATOR - Aggregate (Interface)
 *
 * Interface définissant un catalogue de véhicules avec capacité d'itération.
 * Permet de parcourir les véhicules sans exposer la structure interne.
 *
 * Structure du pattern:
 * - Aggregate: Catalogue (cette interface)
 * - ConcreteAggregate: CatalogueVehicule
 * - Iterator: VehiculeIterator
 * - ConcreteIterator: CatalogueIterator
 *
 * Exemple d'utilisation:
 * Catalogue catalogue = new CatalogueVehicule();
 * catalogue.addVehicule(vehicule1);
 * VehiculeIterator it = catalogue.getIterator();
 * while (it.hasNext()) {
 *     Vehicule v = (Vehicule) it.next();
 * }
 */
public interface Catalogue {

    /**
     * Retourne un itérateur pour parcourir les véhicules
     * @return Un itérateur de véhicules
     */
    VehiculeIterator getIterator();

    /**
     * Ajoute un véhicule au catalogue
     * @param vehicule Le véhicule à ajouter
     */
    void addVehicule(Vehicule vehicule);
}
