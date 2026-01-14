package com.example.med.Iterator;

import java.util.List;

import com.example.med.model.catalogue.Vehicule;

public class CatalogueIterator implements VehiculeIterator {
    private List<Vehicule> liste;
    private int position = 0;

    public CatalogueIterator(List<Vehicule> liste) {
        this.liste = liste;
    }

    @Override
    public boolean hasNext() {
        return position < liste.size() && liste.get(position) != null;
    }

    @Override
    public Object next() {
        return liste.get(position++);
    }

}
