package com.example.med.Iterator;
// import com.example.med.Iterator.VehiculeIterator;
import com.example.med.model.catalogue.Vehicule;
import java.util.List;
import java.util.ArrayList;

public class Catalogue {
    private List<Vehicule> vehicules = new ArrayList<>();


    public void addVehicule(Vehicule vehicule) {
        vehicules.add(vehicule);
    }

    public VehiculeIterator getIterator() {
        return new CatalogueIterator(this.vehicules);
    }

}
