package com.example.med.controller;

import com.example.med.model.commande_et_document.Commande;
import com.example.med.model.commande_et_document.LiasseDocuments;
import com.example.med.model.commande_et_document.TypeFormat;
import com.example.med.outil.adapter.DocumentHtml;
import com.example.med.outil.adapter.DocumentInterface;
import com.example.med.outil.adapter.DocumentPdf;
import com.example.med.repository.CommandeRepository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/template")
@CrossOrigin(origins = "*")
public class DocumentComtroller {
    @Autowired
    private CommandeRepository commandeRepository; 

    //singleton
    @GetMapping("/liasse-type")
    public ResponseEntity<LiasseDocuments> getLiasseVierge() {
        // On récupère l'instance unique définie dans le modèle
        LiasseDocuments liasseVierge = LiasseDocuments.getInstance();
        return ResponseEntity.ok(liasseVierge);
    }


    //adapter pattern
    

    @PostMapping("/imprimer-seul/{format}")
    public ResponseEntity<?> genererFichierUnique(
            @PathVariable TypeFormat format,
            @RequestBody Map<String, Long> payload) { 

        Long id = payload.get("idCommande");
        
        
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));

        
        DocumentInterface adapter = (format == TypeFormat.PDF) ? 
                new DocumentPdf("Document_Unique") : new DocumentHtml("Document_Unique");

        
        adapter.preparerDonnees(commande);
        String urlResultat = adapter.imprimer();

        Map<String, String> response = new HashMap<>();
        response.put("url", urlResultat);
        response.put("format", format.toString());
        return ResponseEntity.ok(response);
    }


}
