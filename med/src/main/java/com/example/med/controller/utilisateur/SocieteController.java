package com.example.med.controller.utilisateur;

import com.example.med.dto.Auth;
import com.example.med.model.utilisateur.Societe;
import com.example.med.repository.SocieteRepository;
import com.example.med.repository.UtilisateurRepository;
import com.example.med.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/societes")
@RequiredArgsConstructor
public class SocieteController {

    private final SocieteRepository repository;
    private final UtilisateurRepository utilisateurRepository;
    private final JwtUtil jwtUtil;

    /**
     * Liste toutes les sociétés
     */
    @GetMapping
    public ResponseEntity<List<Societe>> getAllSocietes() {
        List<Societe> societes = repository.findAll();
        // Masquer les mots de passe
        societes.forEach(s -> s.setMotDePasse(null));
        return ResponseEntity.ok(societes);
    }

    /**
     * Récupère une société par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Societe> getSocieteById(@PathVariable Long id) {
        return repository.findById(id)
            .map(societe -> {
                societe.setMotDePasse(null);
                return ResponseEntity.ok(societe);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Authentification d'une societe (retourne le token JWT)
     */
    @PostMapping("/auth")
    public ResponseEntity<?> authentification(@RequestBody Auth auth) {
        try {
            Optional<Societe> societeOpt = repository.findByEmail(auth.getEmail());

            if (societeOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Email non trouve"));
            }

            Societe societe = societeOpt.get();

            if (BCrypt.checkpw(auth.getMotDePasse(), societe.getMotDePasse())) {
                String token = jwtUtil.generateToken(societe.getIdUtilisateur(), societe.getEmail(), "SOCIETE");
                societe.setMotDePasse(null);
                return ResponseEntity.ok(Map.of(
                    "token", token,
                    "user", societe,
                    "type", "SOCIETE",
                    "message", "Connexion reussie"
                ));
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Mot de passe incorrect"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Erreur lors de l'authentification"));
        }
    }

    /**
     * Crée une nouvelle société
     */
    @PostMapping
    public ResponseEntity<?> createSociete(@RequestBody Societe societe) {
        // Verifier si l'email existe deja (dans toutes les tables utilisateur)
        if (utilisateurRepository.existsByEmail(societe.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("message", "Cet email est deja utilise"));
        }

        // Encoder le mot de passe si présent
        if (societe.getMotDePasse() != null && !societe.getMotDePasse().isEmpty()) {
            societe.setMotDePasse(BCrypt.hashpw(societe.getMotDePasse(), BCrypt.gensalt()));
        }
        Societe saved = repository.save(societe);
        saved.setMotDePasse(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Met à jour une société
     */
    @PutMapping("/{id}")
    public ResponseEntity<Societe> updateSociete(@PathVariable Long id, @RequestBody Societe details) {
        return repository.findById(id)
            .map(existing -> {
                // Champs de base
                if (details.getNom() != null) existing.setNom(details.getNom());
                if (details.getNumeroTaxe() != null) existing.setNumeroTaxe(details.getNumeroTaxe());

                // Coordonnées (nouveaux champs)
                if (details.getTelephone() != null) existing.setTelephone(details.getTelephone());
                if (details.getAdresse() != null) existing.setAdresse(details.getAdresse());
                if (details.getVille() != null) existing.setVille(details.getVille());
                if (details.getPays() != null) existing.setPays(details.getPays());

                // Ne pas modifier le mot de passe ici sauf si explicitement fourni
                if (details.getMotDePasse() != null && !details.getMotDePasse().isEmpty()) {
                    existing.setMotDePasse(BCrypt.hashpw(details.getMotDePasse(), BCrypt.gensalt()));
                }

                Societe saved = repository.save(existing);
                saved.setMotDePasse(null);
                return ResponseEntity.ok(saved);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Supprime une société
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSociete(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
