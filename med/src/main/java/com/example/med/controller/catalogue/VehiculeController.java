package com.example.med.controller.catalogue;

import com.example.med.dto.VehiculeCreationDTO;
import com.example.med.model.catalogue.Vehicule;
import com.example.med.outil.decorator.DecorateurPromo;
import com.example.med.outil.decorator.VehiculeComposant;
import com.example.med.outil.decorator.VehiculeDeBase;
import com.example.med.repository.VehiculeRepository;
import com.example.med.service.vehicule.VehiculeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RepositoryRestController // Indique que ce contrôleur remplace les routes Data REST
@RequiredArgsConstructor
public class VehiculeController {

    private final VehiculeRepository repository;
    @Autowired
    private VehiculeService vehiculeService;

    @PostMapping("/vehicules/")
    public ResponseEntity <String>createVehicule(@RequestBody VehiculeCreationDTO dto) {
        //TODO: process POST request

        String resultat = vehiculeService.createVehicule(dto.getEnergie(), dto.getType()); //energie electrique or essence and type auto or scooter from dto
        return ResponseEntity.ok("Vehicule created successfully");
    }

    // Cette méthode va INTERCEPTER le GET /api/vehicules par défaut
    @GetMapping(path = "/vehicules")
    public @ResponseBody ResponseEntity<?> findAllCustom() {

        //Récupérer les données de la base
        List<Vehicule> vehicules = repository.findAll();

        //Parcourir la liste pour "Décorer" à la volée
        for (Vehicule v : vehicules) {

            // Si le véhicule est en solde, on active le Pattern Decorator
            if (v.isSolde()) {

                // A. On instancie le décorateur autour du véhicule (via VehiculeDeBase)
                VehiculeComposant vehiculeDecore = new DecorateurPromo(new VehiculeDeBase(v));

                v.setPrixBase(vehiculeDecore.getPrix()); // Remplace le prix par le prix réduit
                v.setNom(vehiculeDecore.getNom());      // Remplace le nom par "Nom [PROMO...]"
            }
        }
        return ResponseEntity.ok(vehicules);
    }

//    @PostMapping(path = "/vehicules")
//    public @ResponseBody ResponseEntity<?> createVehicule(@RequestBody Vehicule vehicule) {
//
//        Vehicule saved = repository.save(vehicule);
//        return ResponseEntity
//                .created(URI.create("/api/vehicules/" + saved.getIdVehicule()))
//                .body(saved);
//    }

    @GetMapping(path = "/vehicules/{id}")
    public @ResponseBody ResponseEntity<?> getVehiculeById(@PathVariable Long id) {
        Optional<Vehicule> vehicule = repository.findById(id);

        // Si trouvé, on renvoie 200 OK, sinon 404 Not Found
        return vehicule.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(path = "/vehicules/{id}")
    public @ResponseBody ResponseEntity<?> updateVehicule(@PathVariable Long id, @RequestBody Vehicule details) {
        return repository.findById(id).map(existing -> {
            // Mise à jour de tous les champs
            existing.setNom(details.getNom());
            existing.setMarque(details.getMarque());
            existing.setModel(details.getModel());
            existing.setPrixBase(details.getPrixBase());
            existing.setAnnee(details.getAnnee());
            existing.setEngine(details.getEngine());
            existing.setType(details.getType());
            // Attention : gérer les relations (Stock, Options) ici si nécessaire

            repository.save(existing);
            return ResponseEntity.ok(existing);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping(path = "/vehicules/{id}")
    public @ResponseBody ResponseEntity<?> patchVehicule(@PathVariable Long id, @RequestBody Vehicule updates) {
        return repository.findById(id).map(existing -> {
            if (updates.getNom() != null) existing.setNom(updates.getNom());
            if (updates.getMarque() != null) existing.setMarque(updates.getMarque());
            if (updates.getModel() != null) existing.setModel(updates.getModel());
            if (updates.getPrixBase() != 0) existing.setPrixBase(updates.getPrixBase());
            // ... autres champs

            repository.save(existing);
            return ResponseEntity.ok(existing);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "/vehicules/{id}")
    public @ResponseBody ResponseEntity<?> deleteVehicule(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            // 204 No Content est le standard pour une suppression réussie
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

