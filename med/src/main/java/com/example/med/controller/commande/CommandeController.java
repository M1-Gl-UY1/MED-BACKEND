package com.example.med.controller.commande;

import com.example.med.dto.CalculerMontantRequest;
import com.example.med.dto.SoldeVehiculeRequest;
import com.example.med.model.catalogue.Vehicule;
import com.example.med.model.commande_et_document.Commande;
import com.example.med.model.panier.StatutPanier;
import com.example.med.repository.CommandeRepository;
import com.example.med.repository.VehiculeRepository;
import com.example.med.service.commande.CommandeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/commandes")
public class CommandeController {

    private final CommandeService commandeService;
    private final CommandeRepository commandeRepository;
    private final VehiculeRepository vehiculeRepository;

    public CommandeController( CommandeService commandeService, CommandeRepository commandeRepository, VehiculeRepository vehiculeRepository) {
        this.commandeService = commandeService;
        this.commandeRepository = commandeRepository;
        this.vehiculeRepository = vehiculeRepository;
    }

    // Fsctory
    @PostMapping
    public ResponseEntity <Commande> creerCommande(@RequestParam String typeCommande) {
        Commande commande = commandeService.creerCommande(typeCommande);
        commande.setStatut(StatutPanier.ACTIF);
        return ResponseEntity.ok(commandeRepository.save(commande));
    }

    //TEMPLATE METHOD
    @PostMapping("/{id}/calculer")
    public ResponseEntity<Double> calculerMontant(@PathVariable Long id, @RequestBody CalculerMontantRequest request) {
        Commande commande = commandeRepository.findById(id).orElseThrow(()-> new RuntimeException("Commande introuvable"));
        commande.setPaysLivraison(request.getPaysLivraison());
        double montant = commandeService.calculerMontant(commande, request.getPaysLivraison().name());
        commande.setTotal(montant);
        commandeRepository.save(commande);
        return ResponseEntity.ok(montant);
    }

    // VALIDATION COMMANDE
    @PostMapping("/{id}/valider")
    public ResponseEntity<String> valider(@PathVariable Long id) {
        Commande commande = commandeRepository.findById(id).orElseThrow(() -> new RuntimeException("Commande introuvable"));
        commandeService.validerCommande(commande);
        commandeRepository.save(commande);
        return ResponseEntity.ok("Commande validee");
    }

    // COMMAND PATTERN
    @PostMapping("/vehicules/{id}/solde")
    public ResponseEntity<String> soldeVehicule(@PathVariable Long id, @RequestBody SoldeVehiculeRequest request) {
        Vehicule vehicule = vehiculeRepository.findById(id).orElseThrow(()-> new RuntimeException("Vehicule introuvable"));
        commandeService.appliquerSoldeVehicule(vehicule,  request.getTauxReduction());
        vehiculeRepository.save(vehicule);
        return ResponseEntity.ok("Solde appplique");
    }

    // ANNULATION
    @PostMapping("/annuler")
    public ResponseEntity<String> annulerDerniereAction() {
        commandeService.annulerDerniereAction();
        return ResponseEntity.ok("Derniere action annulee");
    }
}
