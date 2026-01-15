package com.example.med.outil.Iterator;
import com.example.med.model.catalogue.Vehicule;

public interface Catalogue {
    public VehiculeIterator getIterator();
    public void addVehicule(Vehicule vehicule);
}
