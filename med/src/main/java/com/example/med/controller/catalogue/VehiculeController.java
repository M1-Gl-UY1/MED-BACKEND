package com.example.med.controller.catalogue;

import com.example.med.dto.VehiculeCreationDTO;
import com.example.med.model.catalogue.ImageVehicule;
import com.example.med.model.catalogue.Option;
import com.example.med.model.catalogue.Stock;
import com.example.med.model.catalogue.TypeEngine;
import com.example.med.model.catalogue.TypeVehicule;
import com.example.med.model.catalogue.Vehicule;
import com.example.med.outil.decorator.DecorateurPromo;
import com.example.med.outil.decorator.VehiculeComposant;
import com.example.med.outil.decorator.VehiculeDeBase;
import com.example.med.repository.ImageVehiculeRepository;
import com.example.med.repository.OptionRepository;
import com.example.med.repository.StockRepository;
import com.example.med.repository.VehiculeRepository;
import com.example.med.service.storage.FileStorageService;
import com.example.med.service.vehicule.VehiculeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RepositoryRestController // Indique que ce contrôleur remplace les routes Data REST
@RequiredArgsConstructor
@Transactional  // Garder la session Hibernate ouverte pour les collections lazy-loaded
public class VehiculeController {

    private final VehiculeRepository repository;
    private final ImageVehiculeRepository imageRepository;
    private final FileStorageService fileStorageService;
    private final StockRepository stockRepository;
    private final OptionRepository optionRepository;

    @Autowired
    private VehiculeService vehiculeService;

    @PostMapping("/vehicules")
    public ResponseEntity<Vehicule> createVehicule(@RequestBody VehiculeCreationDTO dto) {
        System.out.println("hey !");
        // Créer le véhicule avec les données du DTO
        Vehicule vehicule = new Vehicule();

        // Définir l'énergie (engine)
        if (dto.getEnergie() != null) {
            try {
                vehicule.setEngine(TypeEngine.valueOf(dto.getEnergie().toUpperCase()));
            } catch (IllegalArgumentException e) {
                vehicule.setEngine(TypeEngine.ESSENCE);
            }
        } else {
            vehicule.setEngine(TypeEngine.ESSENCE);
        }

        // Définir le type de véhicule
        if (dto.getType() != null) {
            try {
                String typeStr = dto.getType().toUpperCase();
                if (typeStr.equals("AUTO")) typeStr = "AUTOMOBILE";
                vehicule.setType(TypeVehicule.valueOf(typeStr));
            } catch (IllegalArgumentException e) {
                vehicule.setType(TypeVehicule.AUTOMOBILE);
            }
        } else {
            vehicule.setType(TypeVehicule.AUTOMOBILE);
        }

        // Informations de base
        vehicule.setNom(dto.getNom() != null ? dto.getNom() : "Nouveau véhicule");
        vehicule.setMarque(dto.getMarque() != null ? dto.getMarque() : "");
        vehicule.setModel(dto.getModel() != null ? dto.getModel() : "");
        vehicule.setAnnee(dto.getAnnee() != null ? dto.getAnnee() : java.time.Year.now().getValue());
        vehicule.setPrixBase(dto.getPrixBase() != null ? dto.getPrixBase() : 0);
        vehicule.setDescription(dto.getDescription());

        // Caractéristiques techniques
        vehicule.setPuissance(dto.getPuissance());
        vehicule.setTransmission(dto.getTransmission());
        vehicule.setCarburant(dto.getCarburant());
        vehicule.setConsommation(dto.getConsommation());
        vehicule.setAcceleration(dto.getAcceleration());
        vehicule.setVitesseMax(dto.getVitesseMax());

        // Couleurs disponibles
        if (dto.getCouleurs() != null && !dto.getCouleurs().isEmpty()) {
            vehicule.setCouleurs(new ArrayList<>(dto.getCouleurs()));
        }

        // Statuts
        vehicule.setNouveau(dto.getNouveau() != null ? dto.getNouveau() : true);
        vehicule.setSolde(dto.getSolde() != null ? dto.getSolde() : false);
        vehicule.setFacteurReduction(dto.getFacteurReduction() != null ? dto.getFacteurReduction() : 0);

        // Sauvegarder le véhicule d'abord
        Vehicule saved = repository.save(vehicule);

        // Créer un stock initial si quantité spécifiée
        if (dto.getQuantiteStock() != null && dto.getQuantiteStock() > 0) {
            Stock stock = new Stock();
            stock.setQuantite(dto.getQuantiteStock());
            stock.setDateEntre(java.time.LocalDate.now());
            stock = stockRepository.save(stock);
            saved.setStock(stock);
        }

        // Associer les options
        if (dto.getOptionIds() != null && !dto.getOptionIds().isEmpty()) {
            List<Option> options = optionRepository.findAllById(dto.getOptionIds());
            saved.setOptions(new ArrayList<>(options));
        }

        // Ajouter les images (URLs)
        if (dto.getImageUrls() != null && !dto.getImageUrls().isEmpty()) {
            boolean isFirst = true;
            for (String url : dto.getImageUrls()) {
                ImageVehicule image = new ImageVehicule(url);
                image.setEstPrincipale(isFirst);
                saved.ajouterImage(image);
                isFirst = false;
            }
        }

        // Sauvegarder avec toutes les relations
        saved = repository.save(saved);

        return ResponseEntity.created(URI.create("/vehicules/" + saved.getIdVehicule())).body(saved);
    }

    // Cette méthode va INTERCEPTER le GET /api/vehicules par défaut
    @Transactional(readOnly = true)
    @GetMapping("/vehicules")
    public ResponseEntity<List<Vehicule>> findAllCustom() {

        List<Vehicule> vehicules = repository.findAll();

        for (Vehicule v : vehicules) {
            if (v.isSolde()) {
                VehiculeComposant vehiculeDecore =
                        new DecorateurPromo(new VehiculeDeBase(v));

                v.setPrixBase(vehiculeDecore.getPrix());
                v.setNom(vehiculeDecore.getNom());
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

    @Transactional(readOnly = true)
    @GetMapping("/vehicules/{id}")
    public ResponseEntity<?> getVehiculeById(@PathVariable Long id) {

        return repository.findById(id).map(v -> {

            if (v.isSolde()) {
                VehiculeComposant decore =
                        new DecorateurPromo(new VehiculeDeBase(v));

                v.setNom(decore.getNom());
                v.setPrixBase(decore.getPrix());
            }

            return ResponseEntity.ok(v);
        }).orElse(ResponseEntity.notFound().build());
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

