package com.example.med.controller.documents;

import com.example.med.outil.adapter.DocumentInterface;
import com.example.med.outil.adapter.DocumentPdf;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/documents/adapter")
@CrossOrigin(origins = "*")
public class AdapterController {

    /**
     * TASK 5 : ADAPTER
     * Permet de générer un PDF unique rapidement à partir de n'importe quel texte.
     */
    @PostMapping("/imprimer-document")
    public ResponseEntity<Map<String, String>> imprimerPdfRapide(@RequestBody Map<String, String> body) {
        String titre = body.getOrDefault("titre", "Document_Sans_Titre");
        String contenu = body.get("contenu");

        // Utilisation de l'Adapter
        // On traite le PDF via l'interface standard
        DocumentInterface adapter = new DocumentPdf(titre);
        adapter.setContenu(contenu);
        
        // L'imprimerie nous renvoie l'URL du fichier créé
        String urlFichier = adapter.imprimer();

        Map<String, String> response = new HashMap<>();
        response.put("message", "PDF généré avec succès par l'Adapter");
        response.put("url", urlFichier);
        response.put("format", "PDF/iText");

        return ResponseEntity.ok(response);
    }
}