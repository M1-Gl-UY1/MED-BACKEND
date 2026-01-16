package com.example.med.controller.catalogue;

import com.example.med.dto.VehiculeCreationDTO;
import com.example.med.model.catalogue.ImageVehicule;
import com.example.med.model.catalogue.Vehicule;
import com.example.med.outil.decorator.DecorateurPromo;
import com.example.med.outil.decorator.VehiculeComposant;
import com.example.med.outil.decorator.VehiculeDeBase;
import com.example.med.repository.ImageVehiculeRepository;
import com.example.med.repository.VehiculeRepository;
import com.example.med.service.storage.FileStorageService;
import com.example.med.service.vehicule.VehiculeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RepositoryRestController // Indique que ce contrôleur remplace les routes Data REST
@RequiredArgsConstructor
public class VehiculeController {

    private final VehiculeRepository repository;
    private final ImageVehiculeRepository imageRepository;
    private final FileStorageService fileStorageService;

    @Autowired
    private VehiculeService vehiculeService;

    @PostMapping("/vehicules/")
    public ResponseEntity <String>createVehicule(@RequestBody VehiculeCreationDTO dto) {
        //TODO: process POST request

        String resultat = vehiculeService.createVehicule(dto.getEnergie(), dto.getType()); //energie electrique or essence and type auto or scooter from dto
        return ResponseEntity.ok("Vehicule created successfully");
    }

    // Cette méthode va INTERCEPTER le GET /api/vehicules par défaut
    @GetMapping(path = "/vehicules")
    public @ResponseBody ResponseEntity<?> findAllCustom() {

        //Récupérer les données de la base
        List<Vehicule> vehicules = repository.findAll();

        //Parcourir la liste pour "Décorer" à la volée
        for (Vehicule v : vehicules) {

            // Si le véhicule est en solde, on active le Pattern Decorator
            if (v.isSolde()) {

                // A. On instancie le décorateur autour du véhicule (via VehiculeDeBase)
                VehiculeComposant vehiculeDecore = new DecorateurPromo(new VehiculeDeBase(v));

                v.setPrixBase(vehiculeDecore.getPrix()); // Remplace le prix par le prix réduit
                v.setNom(vehiculeDecore.getNom());      // Remplace le nom par "Nom [PROMO...]"
            }
        }
        return ResponseEntity.ok(vehicules);
    }

//    @PostMapping(path = "/vehicules")
//    public @ResponseBody ResponseEntity<?> createVehicule(@RequestBody Vehicule vehicule) {
//
//        Vehicule saved = repository.save(vehicule);
//        return ResponseEntity
//                .created(URI.create("/api/vehicules/" + saved.getIdVehicule()))
//                .body(saved);
//    }

    @GetMapping(path = "/vehicules/{id}")
    public @ResponseBody ResponseEntity<?> getVehiculeById(@PathVariable Long id) {
        Optional<Vehicule> vehicule = repository.findById(id);

        // Si trouvé, on renvoie 200 OK, sinon 404 Not Found
        return vehicule.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(path = "/vehicules/{id}")
    public @ResponseBody ResponseEntity<?> updateVehicule(@PathVariable Long id, @RequestBody Vehicule details) {
        return repository.findById(id).map(existing -> {
            // Mise à jour de tous les champs
            existing.setNom(details.getNom());
            existing.setMarque(details.getMarque());
            existing.setModel(details.getModel());
            existing.setPrixBase(details.getPrixBase());
            existing.setAnnee(details.getAnnee());
            existing.setEngine(details.getEngine());
            existing.setType(details.getType());
            // Attention : gérer les relations (Stock, Options) ici si nécessaire

            repository.save(existing);
            return ResponseEntity.ok(existing);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping(path = "/vehicules/{id}")
    public @ResponseBody ResponseEntity<?> patchVehicule(@PathVariable Long id, @RequestBody Vehicule updates) {
        return repository.findById(id).map(existing -> {
            if (updates.getNom() != null) existing.setNom(updates.getNom());
            if (updates.getMarque() != null) existing.setMarque(updates.getMarque());
            if (updates.getModel() != null) existing.setModel(updates.getModel());
            if (updates.getPrixBase() != 0) existing.setPrixBase(updates.getPrixBase());
            // ... autres champs

            repository.save(existing);
            return ResponseEntity.ok(existing);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "/vehicules/{id}")
    public @ResponseBody ResponseEntity<?> deleteVehicule(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            // 204 No Content est le standard pour une suppression réussie
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ============================================
    // Gestion des images multiples
    // ============================================

    /**
     * Récupère toutes les images d'un véhicule
     */
    @GetMapping(path = "/vehicules/{id}/images")
    public ResponseEntity<List<ImageVehicule>> getImagesVehicule(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        List<ImageVehicule> images = imageRepository.findByVehiculeIdVehiculeOrderByOrdreAffichageAsc(id);
        return ResponseEntity.ok(images);
    }

    /**
     * Ajoute une image à un véhicule
     */
    @PostMapping(path = "/vehicules/{id}/images")
    public ResponseEntity<ImageVehicule> ajouterImage(
            @PathVariable Long id,
            @RequestBody ImageVehicule image) {
        return repository.findById(id).map(vehicule -> {
            vehicule.ajouterImage(image);
            repository.save(vehicule);
            return ResponseEntity.ok(image);
        }).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Ajoute plusieurs images à un véhicule
     */
    @PostMapping(path = "/vehicules/{id}/images/batch")
    public ResponseEntity<List<ImageVehicule>> ajouterImages(
            @PathVariable Long id,
            @RequestBody List<ImageVehicule> images) {
        return repository.findById(id).map(vehicule -> {
            for (ImageVehicule image : images) {
                vehicule.ajouterImage(image);
            }
            repository.save(vehicule);
            return ResponseEntity.ok(vehicule.getImages());
        }).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Supprime une image d'un véhicule
     */
    @DeleteMapping(path = "/vehicules/{vehiculeId}/images/{imageId}")
    public ResponseEntity<?> supprimerImage(
            @PathVariable Long vehiculeId,
            @PathVariable Long imageId) {
        return repository.findById(vehiculeId).map(vehicule -> {
            return imageRepository.findById(imageId).map(image -> {
                vehicule.retirerImage(image);
                repository.save(vehicule);
                return ResponseEntity.noContent().build();
            }).orElse(ResponseEntity.notFound().build());
        }).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Définit une image comme image principale
     */
    @PatchMapping(path = "/vehicules/{vehiculeId}/images/{imageId}/principale")
    public ResponseEntity<ImageVehicule> setImagePrincipale(
            @PathVariable Long vehiculeId,
            @PathVariable Long imageId) {
        return repository.findById(vehiculeId).map(vehicule -> {
            // Retirer le flag principale de toutes les images
            for (ImageVehicule img : vehicule.getImages()) {
                img.setEstPrincipale(false);
            }
            // Définir la nouvelle image principale
            return imageRepository.findById(imageId).map(image -> {
                image.setEstPrincipale(true);
                repository.save(vehicule);
                return ResponseEntity.ok(image);
            }).orElse(ResponseEntity.notFound().build());
        }).orElse(ResponseEntity.notFound().build());
    }

    // ============================================
    // Upload de fichiers images
    // ============================================

    /**
     * Upload une image pour un véhicule
     */
    @PostMapping(path = "/vehicules/{id}/images/upload")
    public ResponseEntity<ImageVehicule> uploadImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "principale", defaultValue = "false") boolean principale,
            HttpServletRequest request) {

        return repository.findById(id).map(vehicule -> {
            // Stocker le fichier
            String fileName = fileStorageService.storeFile(file);

            // Générer l'URL
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String imageUrl = fileStorageService.getFileUrl(fileName, baseUrl);

            // Créer l'entité ImageVehicule
            ImageVehicule image = new ImageVehicule(imageUrl);
            image.setEstPrincipale(principale);

            // Si c'est la principale, retirer le flag des autres
            if (principale) {
                for (ImageVehicule img : vehicule.getImages()) {
                    img.setEstPrincipale(false);
                }
            }

            vehicule.ajouterImage(image);
            repository.save(vehicule);

            return ResponseEntity.ok(image);
        }).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Upload plusieurs images pour un véhicule
     */
    @PostMapping(path = "/vehicules/{id}/images/upload/batch")
    public ResponseEntity<List<ImageVehicule>> uploadImages(
            @PathVariable Long id,
            @RequestParam("files") MultipartFile[] files,
            HttpServletRequest request) {

        return repository.findById(id).map(vehicule -> {
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            List<ImageVehicule> uploadedImages = new ArrayList<>();

            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                String fileName = fileStorageService.storeFile(file);
                String imageUrl = fileStorageService.getFileUrl(fileName, baseUrl);

                ImageVehicule image = new ImageVehicule(imageUrl);
                // Première image = principale si aucune image existante
                if (i == 0 && vehicule.getImages().isEmpty()) {
                    image.setEstPrincipale(true);
                }

                vehicule.ajouterImage(image);
                uploadedImages.add(image);
            }

            repository.save(vehicule);
            return ResponseEntity.ok(uploadedImages);
        }).orElse(ResponseEntity.notFound().build());
    }
}

