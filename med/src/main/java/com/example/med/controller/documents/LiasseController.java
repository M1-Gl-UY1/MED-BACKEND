package com.example.med.controller.documents;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.med.model.commande_et_document.LiasseDocuments;
import com.example.med.outil.Builder.DirecteurLiasse;
import com.example.med.outil.Builder.LiasseBuilder;
import com.example.med.outil.Builder.LiasseBuilderHTML;
import com.example.med.outil.Builder.LiasseBuilderPDF;

@RestController
@RequestMapping("/api/documents/liasse")
@CrossOrigin(origins = "*") 
public class LiasseController {

    /**
     * TASK 4 : SINGLETON
     * Récupère la "Liasse Vierge" (Instance unique partagée par tous)
     */
        @GetMapping("/vierge")
    public ResponseEntity<LiasseDocuments> getLiasseVierge() {
        
        return ResponseEntity.ok(LiasseDocuments.getInstance());
    }

    /**
     * TASK 2 : BUILDER
     * Génère une liasse complète de documents pour une vente.
     * Exemple de JSON attendu : { "client": "Jean", "vehicule": "Tesla Model 3", "prix": "45000€" }
     */
    @PostMapping("/generer-liasse/{format}")
public ResponseEntity<LiasseDocuments> genererLiasse(
        @PathVariable String format, 
        @RequestBody Map<String, String> data) {

    LiasseBuilder builder;

    // Logique de sélection du Builder (Pattern Strategy/Factory simple)
    if ("html".equalsIgnoreCase(format)) {
        builder = new LiasseBuilderHTML();
    } else {
        builder = new LiasseBuilderPDF();
    }

    DirecteurLiasse directeur = new DirecteurLiasse(builder);
    
    // Orchestration de la construction
    directeur.construireLiasse(data.get("client"), data.get("vehicule"));

    return ResponseEntity.ok(builder.getLiasse());
}
}
