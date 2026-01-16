package com.example.med.service.catalogue;

import com.example.med.model.catalogue.Vehicule;
import com.example.med.outil.decorator.DecorateurPromo;
import com.example.med.outil.decorator.VehiculeComposant;
import com.example.med.outil.decorator.VehiculeDeBase;
import com.example.med.repository.VehiculeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl implements VehiculeService{
    private final VehiculeRepository repository;

    @Override
    public List<VehiculeComposant> lister() {
        List<Vehicule> vehicules = repository.findAll();
        List<VehiculeComposant> result = new ArrayList<>();

        for (Vehicule v : vehicules) {
            if (v.isSolde()) {
                result.add(new DecorateurPromo(new VehiculeDeBase(v)));
            } else {
                result.add(new VehiculeDeBase(v));
            }
        }

        return result;
    }
}
