package com.example.med.controller.auth;

import com.example.med.dto.Auth;
import com.example.med.model.utilisateur.Admin;
import com.example.med.model.utilisateur.Client;
import com.example.med.model.utilisateur.Societe;
import com.example.med.repository.AdminRepository;
import com.example.med.repository.ClientRepository;
import com.example.med.repository.SocieteRepository;
import com.example.med.security.JwtUtil;
import com.example.med.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final ClientRepository clientRepository;
    private final SocieteRepository societeRepository;
    private final AdminRepository adminRepository;
    private final JwtUtil jwtUtil;

    /**
     * Login unifie pour Client et Societe
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Auth auth) {
        // Essayer d'abord comme client
        Optional<Client> clientOpt = clientRepository.findByEmail(auth.getEmail());
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            if (BCrypt.checkpw(auth.getMotDePasse(), client.getMotDePasse())) {
                String token = jwtUtil.generateToken(client.getIdUtilisateur(), client.getEmail(), "CLIENT");
                client.setMotDePasse(null);
                return ResponseEntity.ok(Map.of(
                    "token", token,
                    "user", client,
                    "type", "CLIENT",
                    "message", "Connexion reussie"
                ));
            }
        }

        // Essayer comme societe
        Optional<Societe> societeOpt = societeRepository.findByEmail(auth.getEmail());
        if (societeOpt.isPresent()) {
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
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("message", "Email ou mot de passe incorrect"));
    }

    /**
     * Login Admin
     */
    @PostMapping("/admin/login")
    public ResponseEntity<?> adminLogin(@RequestBody Auth auth) {
        Optional<Admin> adminOpt = adminRepository.findByEmail(auth.getEmail());

        if (adminOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Email ou mot de passe incorrect"));
        }

        Admin admin = adminOpt.get();

        if (!admin.isActif()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("message", "Compte administrateur desactive"));
        }

        if (!BCrypt.checkpw(auth.getMotDePasse(), admin.getMotDePasse())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Email ou mot de passe incorrect"));
        }

        // Mettre a jour la derniere connexion
        admin.setDerniereConnexion(LocalDateTime.now());
        adminRepository.save(admin);

        String token = jwtUtil.generateToken(admin.getIdUtilisateur(), admin.getEmail(), "ADMIN");
        admin.setMotDePasse(null);

        return ResponseEntity.ok(Map.of(
            "token", token,
            "user", admin,
            "type", "ADMIN",
            "message", "Connexion reussie"
        ));
    }

    /**
     * Valider le token et recuperer les infos utilisateur
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Non authentifie"));
        }

        String userType = principal.getUserType();
        Long userId = principal.getUserId();

        switch (userType) {
            case "CLIENT" -> {
                Optional<Client> clientOpt = clientRepository.findById(userId);
                if (clientOpt.isPresent()) {
                    Client client = clientOpt.get();
                    client.setMotDePasse(null);
                    return ResponseEntity.ok(Map.of(
                        "user", client,
                        "type", "CLIENT"
                    ));
                }
            }
            case "SOCIETE" -> {
                Optional<Societe> societeOpt = societeRepository.findById(userId);
                if (societeOpt.isPresent()) {
                    Societe societe = societeOpt.get();
                    societe.setMotDePasse(null);
                    return ResponseEntity.ok(Map.of(
                        "user", societe,
                        "type", "SOCIETE"
                    ));
                }
            }
            case "ADMIN" -> {
                Optional<Admin> adminOpt = adminRepository.findById(userId);
                if (adminOpt.isPresent()) {
                    Admin admin = adminOpt.get();
                    admin.setMotDePasse(null);
                    return ResponseEntity.ok(Map.of(
                        "user", admin,
                        "type", "ADMIN"
                    ));
                }
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("message", "Utilisateur non trouve"));
    }

    /**
     * Rafraichir le token
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Non authentifie"));
        }

        String newToken = jwtUtil.generateToken(
            principal.getUserId(),
            principal.getEmail(),
            principal.getUserType()
        );

        return ResponseEntity.ok(Map.of(
            "token", newToken,
            "message", "Token rafraichi"
        ));
    }

    /**
     * Mettre a jour le profil admin
     */
    @PutMapping("/admin/profile")
    public ResponseEntity<?> updateAdminProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody Map<String, String> updates) {

        if (principal == null || !"ADMIN".equals(principal.getUserType())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("message", "Acces refuse"));
        }

        Optional<Admin> adminOpt = adminRepository.findById(principal.getUserId());
        if (adminOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Admin non trouve"));
        }

        Admin admin = adminOpt.get();

        // Mettre a jour les champs fournis
        if (updates.containsKey("nom")) {
            admin.setNom(updates.get("nom"));
        }
        if (updates.containsKey("telephone")) {
            admin.setTelephone(updates.get("telephone"));
        }
        if (updates.containsKey("adresse")) {
            admin.setAdresse(updates.get("adresse"));
        }
        if (updates.containsKey("ville")) {
            admin.setVille(updates.get("ville"));
        }
        if (updates.containsKey("pays")) {
            admin.setPays(updates.get("pays"));
        }

        adminRepository.save(admin);
        admin.setMotDePasse(null);

        return ResponseEntity.ok(Map.of(
            "user", admin,
            "message", "Profil mis a jour"
        ));
    }

    /**
     * Changer le mot de passe admin
     */
    @PutMapping("/admin/password")
    public ResponseEntity<?> changeAdminPassword(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody Map<String, String> passwords) {

        if (principal == null || !"ADMIN".equals(principal.getUserType())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("message", "Acces refuse"));
        }

        String currentPassword = passwords.get("currentPassword");
        String newPassword = passwords.get("newPassword");

        if (currentPassword == null || newPassword == null) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", "Mot de passe actuel et nouveau requis"));
        }

        if (newPassword.length() < 6) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", "Le nouveau mot de passe doit contenir au moins 6 caracteres"));
        }

        Optional<Admin> adminOpt = adminRepository.findById(principal.getUserId());
        if (adminOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Admin non trouve"));
        }

        Admin admin = adminOpt.get();

        // Verifier le mot de passe actuel
        if (!BCrypt.checkpw(currentPassword, admin.getMotDePasse())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Mot de passe actuel incorrect"));
        }

        // Mettre a jour le mot de passe
        admin.setMotDePasse(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        adminRepository.save(admin);

        return ResponseEntity.ok(Map.of("message", "Mot de passe modifie avec succes"));
    }
}
