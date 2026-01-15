package com.example.med.controller.vehicule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.med.dto.VehiculeCreationDTO;
import com.example.med.service.vehicule.VehiculeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/vehicules")

public class VehiculeController {
    @Autowired
    private VehiculeService vehiculeService;

    @PostMapping("createVehicule")
    public ResponseEntity <String>createVehicule(@RequestBody VehiculeCreationDTO dto) {
        //TODO: process POST request

        String resultat = vehiculeService.createVehicule(dto.getEnergie(), dto.getType()); //energie electrique or essence and type auto or scooter from dto
        return ResponseEntity.ok("Vehicule created successfully");
    }
    

}
