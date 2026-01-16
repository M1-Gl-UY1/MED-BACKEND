package com.example.med.outil.Iterator;

import com.example.med.model.catalogue.Vehicule;

import java.util.List;

/**
 * PATTERN ITERATOR - ConcreteIterator
 *
 * Implémentation concrète de l'itérateur pour parcourir un catalogue.
 * Maintient une position courante et permet un parcours séquentiel.
 */
public class CatalogueIterator implements VehiculeIterator {

    private List<Vehicule> liste;
    private int position = 0;

    /**
     * Constructeur avec la liste à parcourir
     * @param liste La liste des véhicules à itérer
     */
    public CatalogueIterator(List<Vehicule> liste) {
        this.liste = liste;
    }

    @Override
    public boolean hasNext() {
        return position < liste.size() && liste.get(position) != null;
    }

    @Override
    public Object next() {
        if (!hasNext()) {
            throw new java.util.NoSuchElementException("Plus d'éléments à parcourir");
        }
        return liste.get(position++);
    }

    /**
     * Réinitialise l'itérateur au début
     */
    public void reset() {
        position = 0;
    }
}
