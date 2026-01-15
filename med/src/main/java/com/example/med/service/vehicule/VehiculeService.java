package com.example.med.service.vehicule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.med.outil.Abstract_Factory.VehiculeFactory.VehiculeFactory;
import org.springframework.context.ApplicationContext;

import com.example.med.outil.Abstract_Factory.Automobile.Automobile;
import com.example.med.outil.Abstract_Factory.VehiculeFactory.VehiculeElectricF;
import com.example.med.outil.Abstract_Factory.VehiculeFactory.VehiculeEssenceF;


@Service
public class VehiculeService {
    @Autowired
    private ApplicationContext context; // Pour recuperer la bonne factory dynamiquement selon le type de vehicule
    
    public String createVehicule(String energie, String type) {
    // selection de la factory selon l'energie
      VehiculeFactory factory = (energie.equalsIgnoreCase("electrique")) 
            ? context.getBean(VehiculeElectricF.class) 
            : context.getBean(VehiculeEssenceF.class);

            if (type.equalsIgnoreCase("auto")) {
            Automobile auto = factory.createAutomobile();
            // Ici, tu pourrais transformer l'objet en Entité Vehicule et le sauver en BD via repository
            return "Automobile " + energie + " créée.";
        } else {
            factory.createScooter();
            return "Scooter " + energie + " créé.";
        }

    }
}
