package com.example.med.service.catalogue;

import com.example.med.model.catalogue.Vehicule;
import com.example.med.outil.decorator.VehiculeComposant;

import java.util.List;

public interface VehiculeService {
    List<VehiculeComposant> lister();
}
