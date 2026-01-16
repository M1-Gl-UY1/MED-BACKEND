package com.example.med.controller.utilisateur;

import com.example.med.dto.Auth;
import com.example.med.model.utilisateur.Client;
import com.example.med.repository.ClientRepository;
import com.example.med.security.JwtUtil;
import com.example.med.service.utilisateur.ClientServiceImpl;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientServiceImpl service;
    private final ClientRepository repository;
    private final JwtUtil jwtUtil;

    /**
     * Liste tous les clients
     */
    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = repository.findAll();
        // Masquer les mots de passe
        clients.forEach(c -> c.setMotDePasse(null));
        return ResponseEntity.ok(clients);
    }

    /**
     * Récupère un client par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        return repository.findById(id)
            .map(client -> {
                client.setMotDePasse(null);
                return ResponseEntity.ok(client);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Authentification simple (retourne boolean) - maintenue pour compatibilité
     */
    @PostMapping("/auth/check")
    public boolean authentificationCheck(@RequestBody Auth auth){
        return service.auth(auth.getEmail(), auth.getMotDePasse());
    }

    /**
     * Authentification complete (retourne l'utilisateur et le token JWT)
     */
    @PostMapping("/auth")
    public ResponseEntity<?> authentification(@RequestBody Auth auth){
        try {
            Client client = service.authentifier(auth.getEmail(), auth.getMotDePasse());
            String token = jwtUtil.generateToken(client.getIdUtilisateur(), client.getEmail(), "CLIENT");
            return ResponseEntity.ok(Map.of(
                "token", token,
                "user", client,
                "type", "CLIENT",
                "message", "Connexion reussie"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", e.getMessage()));
        }
    }

    /**
     * Cree un nouveau client
     */
    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        // Encoder le mot de passe avec BCrypt
        if (client.getMotDePasse() != null && !client.getMotDePasse().isEmpty()) {
            client.setMotDePasse(BCrypt.hashpw(client.getMotDePasse(), BCrypt.gensalt()));
        }
        Client saved = repository.save(client);
        saved.setMotDePasse(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Met à jour un client
     */
    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client details) {
        return repository.findById(id)
            .map(existing -> {
                // Champs de base
                if (details.getNom() != null) existing.setNom(details.getNom());
                if (details.getPrenom() != null) existing.setPrenom(details.getPrenom());
                if (details.getSexe() != null) existing.setSexe(details.getSexe());
                if (details.getDateNaissance() != null) existing.setDateNaissance(details.getDateNaissance());

                // Coordonnées (nouveaux champs)
                if (details.getTelephone() != null) existing.setTelephone(details.getTelephone());
                if (details.getAdresse() != null) existing.setAdresse(details.getAdresse());
                if (details.getVille() != null) existing.setVille(details.getVille());
                if (details.getPays() != null) existing.setPays(details.getPays());

                // Hasher le mot de passe si explicitement fourni
                if (details.getMotDePasse() != null && !details.getMotDePasse().isEmpty()) {
                    existing.setMotDePasse(BCrypt.hashpw(details.getMotDePasse(), BCrypt.gensalt()));
                }

                Client saved = repository.save(existing);
                saved.setMotDePasse(null);
                return ResponseEntity.ok(saved);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Supprime un client
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

