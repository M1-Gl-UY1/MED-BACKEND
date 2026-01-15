package com.example.med.service.vehicule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.example.med.model.catalogue.Vehicule;
import com.example.med.outil.Iterator.CatalogueVehicule;
import com.example.med.repository.VehiculeRepository;
import com.example.med.outil.Iterator.VehiculeIterator;
import com.example.med.dto.VehiculeIteratorDTO;
import java.util.ArrayList;

@Service
public class CatalogueService {
    @Autowired
    private VehiculeRepository vehiculeRepository;
    // on recupere les donnees brutes de la base de donnees

    public List<VehiculeIteratorDTO> obtenirCataloguePourClient(){
        // Recuperation des vehicules depuis la base de donnees
        List<Vehicule> vehiculesBD = vehiculeRepository.findAll();
        // Creation du catalogue et ajout des vehicules
        CatalogueVehicule catalogue = new CatalogueVehicule();

        for (Vehicule v : vehiculesBD) {
            catalogue.addVehicule(v);
        }
    // on parcour le catalogue via l'itterator

    List<VehiculeIteratorDTO> listeAEnvoyer = new ArrayList<>();
    VehiculeIterator iterator = catalogue.getIterator();
    while (iterator.hasNext()) {
        Vehicule v = (Vehicule) iterator.next();
        // Transformation en DTO pour l'envoi au client
        listeAEnvoyer.add(new VehiculeIteratorDTO(v.getNom(), v.getPrix(0)) );
    }
    return listeAEnvoyer;
    }
}