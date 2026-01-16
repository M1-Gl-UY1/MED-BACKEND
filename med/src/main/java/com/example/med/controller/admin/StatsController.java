package com.example.med.controller.admin;

import com.example.med.model.catalogue.Vehicule;
import com.example.med.model.commande_et_document.Commande;
import com.example.med.model.commande_et_document.PaysLivraison;
import com.example.med.model.panier.StatutPanier;
import com.example.med.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Contrôleur pour les statistiques du dashboard admin
 */
@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final CommandeRepository commandeRepository;
    private final VehiculeRepository vehiculeRepository;
    private final ClientRepository clientRepository;
    private final SocieteRepository societeRepository;
    private final StockRepository stockRepository;

    /**
     * Statistiques globales du dashboard
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getStats() {
        List<Commande> commandes = commandeRepository.findAll();
        List<Vehicule> vehicules = vehiculeRepository.findAll();

        // Calculs des statistiques
        long totalCommandes = commandes.size();
        long commandesEnCours = commandes.stream()
            .filter(c -> c.getStatut() == StatutPanier.ACTIF)
            .count();
        long commandesValidees = commandes.stream()
            .filter(c -> c.getStatut() == StatutPanier.VALIDEE)
            .count();
        long commandesConverties = commandes.stream()
            .filter(c -> c.getStatut() == StatutPanier.CONVERTI)
            .count();

        double totalVentes = commandes.stream()
            .filter(c -> c.getStatut() == StatutPanier.CONVERTI || c.getStatut() == StatutPanier.VALIDEE)
            .mapToDouble(Commande::getTotal)
            .sum();

        long nombreClients = clientRepository.count();
        long nombreSocietes = societeRepository.count();
        long nombreVehicules = vehicules.size();

        int stockTotal = vehicules.stream()
            .filter(v -> v.getStock() != null)
            .mapToInt(v -> v.getStock().getQuantite())
            .sum();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalVentes", totalVentes);
        stats.put("nombreCommandes", totalCommandes);
        stats.put("commandesEnCours", commandesEnCours);
        stats.put("commandesValidees", commandesValidees);
        stats.put("commandesConverties", commandesConverties);
        stats.put("nombreClients", nombreClients + nombreSocietes);
        stats.put("nombreVehicules", nombreVehicules);
        stats.put("stockTotal", stockTotal);

        return ResponseEntity.ok(stats);
    }

    /**
     * Ventes par pays
     */
    @GetMapping("/ventes-par-pays")
    public ResponseEntity<List<Map<String, Object>>> getVentesParPays() {
        List<Commande> commandes = commandeRepository.findAll();

        Map<PaysLivraison, Double> ventesParPays = commandes.stream()
            .filter(c -> c.getPaysLivraison() != null)
            .collect(Collectors.groupingBy(
                Commande::getPaysLivraison,
                Collectors.summingDouble(Commande::getTotal)
            ));

        double total = ventesParPays.values().stream().mapToDouble(Double::doubleValue).sum();

        List<Map<String, Object>> result = ventesParPays.entrySet().stream()
            .map(entry -> {
                Map<String, Object> map = new HashMap<>();
                map.put("pays", entry.getKey().name());
                map.put("montant", entry.getValue());
                map.put("pourcentage", total > 0 ? Math.round((entry.getValue() / total) * 100) : 0);
                return map;
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    /**
     * Véhicules avec stock faible
     */
    @GetMapping("/stock-faible")
    public ResponseEntity<List<Vehicule>> getStockFaible(@RequestParam(defaultValue = "3") int seuil) {
        List<Vehicule> vehicules = vehiculeRepository.findAll();

        List<Vehicule> stockFaible = vehicules.stream()
            .filter(v -> v.getStock() != null && v.getStock().getQuantite() <= seuil)
            .sorted(Comparator.comparingInt(v -> v.getStock().getQuantite()))
            .collect(Collectors.toList());

        return ResponseEntity.ok(stockFaible);
    }

    /**
     * Commandes récentes
     */
    @GetMapping("/commandes-recentes")
    public ResponseEntity<List<Commande>> getCommandesRecentes(@RequestParam(defaultValue = "5") int limit) {
        List<Commande> commandes = commandeRepository.findAll();

        List<Commande> recentes = commandes.stream()
            .sorted((c1, c2) -> {
                if (c1.getDate() == null) return 1;
                if (c2.getDate() == null) return -1;
                return c2.getDate().compareTo(c1.getDate());
            })
            .limit(limit)
            .collect(Collectors.toList());

        return ResponseEntity.ok(recentes);
    }

    /**
     * Statistiques complètes du dashboard
     */
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardComplet() {
        Map<String, Object> dashboard = new HashMap<>();

        // Stats générales
        dashboard.put("stats", getStats().getBody());
        dashboard.put("ventesParPays", getVentesParPays().getBody());
        dashboard.put("stockFaible", getStockFaible(3).getBody());
        dashboard.put("commandesRecentes", getCommandesRecentes(5).getBody());

        return ResponseEntity.ok(dashboard);
    }
}
