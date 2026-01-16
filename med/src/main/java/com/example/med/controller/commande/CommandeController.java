package com.example.med.controller.commande;

import com.example.med.dto.CalculerMontantRequest;
import com.example.med.dto.CreerCommandeCompleteRequest;
import com.example.med.dto.SoldeVehiculeRequest;
import com.example.med.model.catalogue.Option;
import com.example.med.model.catalogue.Vehicule;
import com.example.med.model.commande_et_document.Commande;
import com.example.med.model.commande_et_document.LigneCommande;
import com.example.med.model.commande_et_document.PaysLivraison;
import com.example.med.model.panier.StatutPanier;
import com.example.med.model.utilisateur.Utilisateur;
import com.example.med.repository.CommandeRepository;
import com.example.med.repository.LigneCommandeRepository;
import com.example.med.repository.OptionRepository;
import com.example.med.repository.UtilisateurRepository;
import com.example.med.repository.VehiculeRepository;
import com.example.med.security.UserPrincipal;
import com.example.med.service.commande.CommandeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/commandes")
@Transactional  // Garder la session Hibernate ouverte pour les collections lazy-loaded
public class CommandeController {

    private final CommandeService commandeService;
    private final CommandeRepository commandeRepository;
    private final VehiculeRepository vehiculeRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final LigneCommandeRepository ligneCommandeRepository;
    private final OptionRepository optionRepository;

    // Taux de TVA par pays
    private static final java.util.Map<PaysLivraison, Double> TAUX_TVA = java.util.Map.of(
        PaysLivraison.CM, 19.25,
        PaysLivraison.FR, 20.0,
        PaysLivraison.US, 8.0,
        PaysLivraison.NG, 7.5
    );

    public CommandeController(
            CommandeService commandeService,
            CommandeRepository commandeRepository,
            VehiculeRepository vehiculeRepository,
            UtilisateurRepository utilisateurRepository,
            LigneCommandeRepository ligneCommandeRepository,
            OptionRepository optionRepository) {
        this.commandeService = commandeService;
        this.commandeRepository = commandeRepository;
        this.vehiculeRepository = vehiculeRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.ligneCommandeRepository = ligneCommandeRepository;
        this.optionRepository = optionRepository;
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

    /**
     * Récupère les commandes d'un utilisateur par son ID
     */
    @GetMapping("/utilisateur/{utilisateurId}")
    public ResponseEntity<List<Commande>> getCommandesByUtilisateur(@PathVariable Long utilisateurId) {
        List<Commande> commandes = commandeRepository.findAll().stream()
            .filter(c -> c.getUtilisateur() != null &&
                        c.getUtilisateur().getIdUtilisateur() == utilisateurId.longValue())
            .sorted((c1, c2) -> {
                if (c1.getDate() == null) return 1;
                if (c2.getDate() == null) return -1;
                return c2.getDate().compareTo(c1.getDate());
            })
            .toList();
        return ResponseEntity.ok(commandes);
    }

    // Factory - Simple (pour compatibilité)
    @PostMapping
    public ResponseEntity <Commande> creerCommande(@RequestParam String typeCommande) {
        Commande commande = commandeService.creerCommande(typeCommande);
        commande.setStatut(StatutPanier.ACTIF);
        return ResponseEntity.ok(commandeRepository.save(commande));
    }

    /**
     * Créer une commande complète avec véhicules, options et utilisateur authentifié
     * Utilise Factory Method pattern pour créer le bon type de commande
     */
    @PostMapping("/creer")
    public ResponseEntity<Commande> creerCommandeComplete(@RequestBody CreerCommandeCompleteRequest request) {
        // Récupérer l'utilisateur authentifié depuis le contexte de sécurité
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) {
            return ResponseEntity.status(401).build();
        }

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getUserId();

        // Récupérer l'utilisateur depuis la base de données
        Utilisateur utilisateur = utilisateurRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Factory Method - Créer le bon type de commande (comptant ou crédit)
        String typeCommande = request.getType() != null ? request.getType().toLowerCase() : "comptant";
        Commande commande = commandeService.creerCommande(typeCommande);

        // Générer une référence unique
        String reference = "CMD-" + java.time.Year.now().getValue() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // Configurer la commande
        commande.setReference(reference);
        commande.setStatut(StatutPanier.ACTIF);
        commande.setDate(LocalDate.now());
        commande.setUtilisateur(utilisateur);
        commande.setPaysLivraison(request.getPaysLivraison() != null ? request.getPaysLivraison() : PaysLivraison.CM);
        commande.setAdresseLivraison(request.getAdresseLivraison());
        commande.setTypePaiement(request.getMethodePaiement());

        // Sauvegarder la commande pour obtenir l'ID
        commande = commandeRepository.save(commande);

        // Calculer le taux de TVA pour le pays
        double tauxTVA = TAUX_TVA.getOrDefault(commande.getPaysLivraison(), 19.25);

        // Créer les lignes de commande
        List<LigneCommande> lignesCommande = new ArrayList<>();
        double totalHT = 0;

        if (request.getLignes() != null) {
            for (CreerCommandeCompleteRequest.LigneCommandeRequest ligneReq : request.getLignes()) {
                // Récupérer le véhicule
                Vehicule vehicule = vehiculeRepository.findById(ligneReq.getVehiculeId())
                    .orElseThrow(() -> new RuntimeException("Véhicule non trouvé: " + ligneReq.getVehiculeId()));

                LigneCommande ligne = new LigneCommande();
                ligne.setVehicule(vehicule);
                ligne.setQuantite(ligneReq.getQuantite());
                ligne.setTauxTVA(tauxTVA);
                ligne.setCommande(commande);

                // Calculer le prix unitaire (avec réduction si applicable)
                double prixUnitaire = vehicule.getPrixBase();
                if (vehicule.isSolde() && vehicule.getFacteurReduction() > 0) {
                    prixUnitaire = prixUnitaire * (1 - vehicule.getFacteurReduction());
                }

                // Ajouter le prix des options
                List<Option> options = new ArrayList<>();
                if (ligneReq.getOptionIds() != null && !ligneReq.getOptionIds().isEmpty()) {
                    options = optionRepository.findAllById(ligneReq.getOptionIds());
                    for (Option option : options) {
                        prixUnitaire += option.getPrix();
                    }
                }

                ligne.setPrixUnitaireHT(prixUnitaire);
                ligne.setOptionsAchetees(options);

                // Sauvegarder la ligne de commande
                ligne = ligneCommandeRepository.save(ligne);
                lignesCommande.add(ligne);

                totalHT += prixUnitaire * ligneReq.getQuantite();
            }
        }

        // Sauvegarder les lignes pour permettre le calcul via Template Method
        commande.setLignesCommandes(lignesCommande);
        commande = commandeRepository.save(commande);

        // TEMPLATE METHOD PATTERN - Calculer le total avec taxes selon le pays
        String codePays = commande.getPaysLivraison() != null ? commande.getPaysLivraison().name() : "CM";
        double totalTTC = commandeService.calculerMontant(commande, codePays);

        // Stocker le total calculé
        commande.setTaxe(totalTTC - totalHT);  // Taxes = TTC - HT
        commande.setTotal(totalTTC);

        // Sauvegarder la commande finale
        commande = commandeRepository.save(commande);

        return ResponseEntity.ok(commande);
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
