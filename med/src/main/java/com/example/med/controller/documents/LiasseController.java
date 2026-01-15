package com.example.med.controller.documents;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.med.model.commande_et_document.LiasseDocuments;
import com.example.med.outil.Builder.DirecteurLiasse;
import com.example.med.outil.Builder.LiasseBuilder;
import com.example.med.outil.Builder.LiasseBuilderPDF;

@RestController
@RequestMapping("/api/documents/liasse")
@CrossOrigin(origins = "*") // Permet au front de se connecter sans erreur CORS
public class LiasseController {

    /**
     * TASK 4 : SINGLETON
     * Récupère la "Liasse Vierge" (Instance unique partagée par tous)
     */
    @GetMapping("/vierge")
    public ResponseEntity<LiasseDocuments> getLiasseVierge() {
        // Appelle le pattern Singleton
        LiasseDocuments instanceUnique = LiasseDocuments.getInstance();
        return ResponseEntity.ok(instanceUnique);
    }

    /**
     * TASK 2 : BUILDER
     * Génère une liasse complète de documents pour une vente.
     * Exemple de JSON attendu : { "client": "Jean", "vehicule": "Tesla Model 3", "prix": "45000€" }
     */
    @PostMapping("/generer")
    public ResponseEntity<LiasseDocuments> genererLiasseComplete(@RequestBody Map<String, String> data) {
        
        // 1. Choix du Builder (ici PDF par défaut, on pourrait ajouter une condition pour HTML)
        LiasseBuilder builder = new LiasseBuilderPDF();
        
        // 2. Utilisation du Directeur pour orchestrer la construction
        DirecteurLiasse directeur = new DirecteurLiasse(builder);
        
        // 3. Préparation du contenu dynamique
        String infos = "Client : " + data.get("client") + 
                       "\nVéhicule : " + data.get("vehicule") + 
                       "\nPrix : " + data.get("prix");

        // 4. Construction (Le builder va appeler l'Adapter pour créer les fichiers PDF)
        builder.creerNouvelleLiasse();
        builder.construireBonCommande("BON DE COMMANDE - " + infos);
        builder.construireCertificatCession("CERTIFICAT DE CESSION - " + infos);
        builder.construireDemandeImmatriculation("DEMANDE IMMATRICULATION - " + infos);

        LiasseDocuments liasseFinale = builder.getLiasse();
        
        // Note : Ici tu pourrais faire liasseRepository.save(liasseFinale) pour la garder en BDD
        
        return ResponseEntity.ok(liasseFinale);
    }
}
