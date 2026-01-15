package com.example.med.controller.vehicule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.med.service.vehicule.CatalogueService;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.med.dto.VehiculeIteratorDTO;
import java.util.List;


@RestController
@RequestMapping("/api/catalogue")

public class CatalogueController {
    @Autowired
    private CatalogueService catalogueService;

    @GetMapping("/liste")

    public ResponseEntity<List<VehiculeIteratorDTO>> getCatalogue(){
        List<VehiculeIteratorDTO> catalogue = catalogueService.obtenirCataloguePourClient();
        return ResponseEntity.ok(catalogue);
    }
    
}
