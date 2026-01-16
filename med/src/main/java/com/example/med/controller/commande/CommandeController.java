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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/commandes")
@Transactional  // Garder la session Hibernate ouverte pour les collections lazy-loaded
public class CommandeController {

    private final CommandeService commandeService;
    private final CommandeRepository commandeRepository;
    private final VehiculeRepository vehiculeRepository;

    public CommandeController( CommandeService commandeService, CommandeRepository commandeRepository, VehiculeRepository vehiculeRepository) {
        this.commandeService = commandeService;
        this.commandeRepository = commandeRepository;
        this.vehiculeRepository = vehiculeRepository;
    }

    /**
     * Liste toutes les commandes
     */
    @GetMapping
    public ResponseEntity<List<Commande>> getAllCommandes() {
        List<Commande> commandes = commandeRepository.findAll();
        // Trier par date décroissante
        commandes.sort((c1, c2) -> {
            if (c1.getDate() == null) return 1;
            if (c2.getDate() == null) return -1;
            return c2.getDate().compareTo(c1.getDate());
        });
        return ResponseEntity.ok(commandes);
    }

    /**
     * Récupère une commande par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Commande> getCommandeById(@PathVariable Long id) {
        return commandeRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère les commandes récentes
     */
    @GetMapping("/recentes")
    public ResponseEntity<List<Commande>> getCommandesRecentes(@RequestParam(defaultValue = "5") int limit) {
        List<Commande> commandes = commandeRepository.findAll();
        commandes.sort((c1, c2) -> {
            if (c1.getDate() == null) return 1;
            if (c2.getDate() == null) return -1;
            return c2.getDate().compareTo(c1.getDate());
        });
        return ResponseEntity.ok(commandes.stream().limit(limit).toList());
    }

    // Factory
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
