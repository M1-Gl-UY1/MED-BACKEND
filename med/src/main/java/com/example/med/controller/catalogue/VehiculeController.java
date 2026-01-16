package com.example.med.controller.catalogue;

import com.example.med.dto.VehiculeCreationDTO;
import com.example.med.model.catalogue.ImageVehicule;
import com.example.med.model.catalogue.Option;
import com.example.med.model.catalogue.Stock;
import com.example.med.model.catalogue.TypeEngine;
import com.example.med.model.catalogue.TypeVehicule;
import com.example.med.model.catalogue.Vehicule;
import com.example.med.outil.Iterator.Catalogue;
import com.example.med.outil.Iterator.CatalogueVehicule;
import com.example.med.outil.Iterator.VehiculeIterator;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RepositoryRestController
@RequiredArgsConstructor
@Transactional
public class VehiculeController {

    private final VehiculeRepository repository;
    private final ImageVehiculeRepository imageRepository;
    private final FileStorageService fileStorageService;
    private final StockRepository stockRepository;
    private final OptionRepository optionRepository;

    @Autowired
    private VehiculeService vehiculeService;

    /**
     * Créer un véhicule
     */
    @PostMapping("/vehicules")
    public ResponseEntity<Vehicule> createVehicule(@RequestBody VehiculeCreationDTO dto) {

        Vehicule vehicule = new Vehicule();

        // Définir le type (AUTOMOBILE ou SCOOTER)
        String type = dto.getType() != null ? dto.getType().toUpperCase() : "AUTOMOBILE";
        if (type.equals("AUTO")) type = "AUTOMOBILE";
        vehicule.setType(type.equals("SCOOTER") ? TypeVehicule.SCOOTER : TypeVehicule.AUTOMOBILE);

        // Définir l'énergie
        String energie = dto.getEnergie() != null ? dto.getEnergie().toUpperCase() : "ESSENCE";
        vehicule.setEngine(energie.equals("ELECTRIQUE") ? TypeEngine.ELECTRIQUE : TypeEngine.ESSENCE);

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

    /**
     * Récupère tous les véhicules avec décoration (Pattern Decorator)
     * On retourne une Map pour ne pas modifier les entités en BD
     */
    @GetMapping("/vehicules")
    @Transactional(readOnly = true)
    public ResponseEntity<?> findAllCustom() {

        List<Vehicule> vehicules = repository.findAll();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Vehicule v : vehicules) {
            Map<String, Object> vehiculeData = new HashMap<>();

            // Copier les données de base
            vehiculeData.put("idVehicule", v.getIdVehicule());
            vehiculeData.put("marque", v.getMarque());
            vehiculeData.put("model", v.getModel());
            vehiculeData.put("annee", v.getAnnee());
            vehiculeData.put("type", v.getType());
            vehiculeData.put("engine", v.getEngine());
            vehiculeData.put("description", v.getDescription());
            vehiculeData.put("puissance", v.getPuissance());
            vehiculeData.put("transmission", v.getTransmission());
            vehiculeData.put("carburant", v.getCarburant());
            vehiculeData.put("consommation", v.getConsommation());
            vehiculeData.put("acceleration", v.getAcceleration());
            vehiculeData.put("vitesseMax", v.getVitesseMax());
            vehiculeData.put("couleurs", v.getCouleurs());
            vehiculeData.put("images", v.getImages());
            vehiculeData.put("options", v.getOptions());
            vehiculeData.put("stock", v.getStock());
            vehiculeData.put("nouveau", v.isNouveau());
            vehiculeData.put("solde", v.isSolde());
            vehiculeData.put("facteurReduction", v.getFacteurReduction());

            // Prix et nom originaux (stockés en BD)
            vehiculeData.put("prixOriginal", v.getPrixBase());
            vehiculeData.put("nomOriginal", v.getNom());

            // Si le véhicule est en solde, appliquer le Pattern Decorator
            if (v.isSolde()) {
                VehiculeComposant vehiculeDecore = new DecorateurPromo(new VehiculeDeBase(v));
                vehiculeData.put("prixBase", vehiculeDecore.getPrix());
                vehiculeData.put("nom", vehiculeDecore.getNom());
                vehiculeData.put("decorated", true);
            } else {
                vehiculeData.put("prixBase", v.getPrixBase());
                vehiculeData.put("nom", v.getNom());
                vehiculeData.put("decorated", false);
            }

            response.add(vehiculeData);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * PATTERN ITERATOR - Parcours du catalogue avec itérateur personnalisé
     */
    @GetMapping("/vehicules/catalogue/iterate")
    @Transactional(readOnly = true)
    public ResponseEntity<?> iterateCatalogue(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String energie) {

        Catalogue catalogue = new CatalogueVehicule();
        List<Vehicule> allVehicules = repository.findAll();
        for (Vehicule v : allVehicules) {
            catalogue.addVehicule(v);
        }

        VehiculeIterator iterator = catalogue.getIterator();
        List<Map<String, Object>> result = new ArrayList<>();

        while (iterator.hasNext()) {
            Vehicule v = (Vehicule) iterator.next();

            boolean matchType = (type == null || v.getType().name().equalsIgnoreCase(type));
            boolean matchEnergie = (energie == null || v.getEngine().name().equalsIgnoreCase(energie));

            if (matchType && matchEnergie) {
                Map<String, Object> vehiculeData = new HashMap<>();
                vehiculeData.put("idVehicule", v.getIdVehicule());
                vehiculeData.put("marque", v.getMarque());
                vehiculeData.put("model", v.getModel());
                vehiculeData.put("annee", v.getAnnee());
                vehiculeData.put("type", v.getType());
                vehiculeData.put("engine", v.getEngine());
                vehiculeData.put("images", v.getImages());
                vehiculeData.put("stock", v.getStock());
                vehiculeData.put("solde", v.isSolde());
                vehiculeData.put("facteurReduction", v.getFacteurReduction());

                if (v.isSolde()) {
                    VehiculeComposant vehiculeDecore = new DecorateurPromo(new VehiculeDeBase(v));
                    vehiculeData.put("prixBase", vehiculeDecore.getPrix());
                    vehiculeData.put("nom", vehiculeDecore.getNom());
                    vehiculeData.put("prixOriginal", v.getPrixBase());
                    vehiculeData.put("decorated", true);
                } else {
                    vehiculeData.put("prixBase", v.getPrixBase());
                    vehiculeData.put("nom", v.getNom());
                    vehiculeData.put("decorated", false);
                }

                result.add(vehiculeData);
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("pattern", "Iterator + Decorator");
        response.put("total", result.size());
        response.put("filtres", Map.of(
            "type", type != null ? type : "tous",
            "energie", energie != null ? energie : "tous"
        ));
        response.put("vehicules", result);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/vehicules/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<?> getVehiculeById(@PathVariable Long id) {
        return repository.findById(id).map(v -> {
            Map<String, Object> vehiculeData = new HashMap<>();

            vehiculeData.put("idVehicule", v.getIdVehicule());
            vehiculeData.put("marque", v.getMarque());
            vehiculeData.put("model", v.getModel());
            vehiculeData.put("annee", v.getAnnee());
            vehiculeData.put("type", v.getType());
            vehiculeData.put("engine", v.getEngine());
            vehiculeData.put("description", v.getDescription());
            vehiculeData.put("puissance", v.getPuissance());
            vehiculeData.put("transmission", v.getTransmission());
            vehiculeData.put("carburant", v.getCarburant());
            vehiculeData.put("consommation", v.getConsommation());
            vehiculeData.put("acceleration", v.getAcceleration());
            vehiculeData.put("vitesseMax", v.getVitesseMax());
            vehiculeData.put("couleurs", v.getCouleurs());
            vehiculeData.put("images", v.getImages());
            vehiculeData.put("options", v.getOptions());
            vehiculeData.put("stock", v.getStock());
            vehiculeData.put("nouveau", v.isNouveau());
            vehiculeData.put("solde", v.isSolde());
            vehiculeData.put("facteurReduction", v.getFacteurReduction());
            vehiculeData.put("prixOriginal", v.getPrixBase());
            vehiculeData.put("nomOriginal", v.getNom());

            if (v.isSolde()) {
                VehiculeComposant decore = new DecorateurPromo(new VehiculeDeBase(v));
                vehiculeData.put("nom", decore.getNom());
                vehiculeData.put("prixBase", decore.getPrix());
                vehiculeData.put("decorated", true);
            } else {
                vehiculeData.put("nom", v.getNom());
                vehiculeData.put("prixBase", v.getPrixBase());
                vehiculeData.put("decorated", false);
            }

            return ResponseEntity.ok(vehiculeData);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/vehicules/{id}")
    public ResponseEntity<?> updateVehicule(@PathVariable Long id, @RequestBody Vehicule details) {
        return repository.findById(id).map(existing -> {
            existing.setNom(details.getNom());
            existing.setMarque(details.getMarque());
            existing.setModel(details.getModel());
            existing.setPrixBase(details.getPrixBase());
            existing.setAnnee(details.getAnnee());
            existing.setEngine(details.getEngine());
            existing.setType(details.getType());

            repository.save(existing);
            return ResponseEntity.ok(existing);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/vehicules/{id}")
    public ResponseEntity<?> patchVehicule(@PathVariable Long id, @RequestBody Vehicule updates) {
        return repository.findById(id).map(existing -> {
            if (updates.getNom() != null) existing.setNom(updates.getNom());
            if (updates.getMarque() != null) existing.setMarque(updates.getMarque());
            if (updates.getModel() != null) existing.setModel(updates.getModel());
            if (updates.getPrixBase() != 0) existing.setPrixBase(updates.getPrixBase());

            repository.save(existing);
            return ResponseEntity.ok(existing);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/vehicules/{id}")
    public ResponseEntity<?> deleteVehicule(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ============================================
    // Gestion des images multiples
    // ============================================

    @GetMapping("/vehicules/{id}/images")
    public ResponseEntity<List<ImageVehicule>> getImagesVehicule(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        List<ImageVehicule> images = imageRepository.findByVehiculeIdVehiculeOrderByOrdreAffichageAsc(id);
        return ResponseEntity.ok(images);
    }

    @PostMapping("/vehicules/{id}/images")
    public ResponseEntity<ImageVehicule> ajouterImage(
            @PathVariable Long id,
            @RequestBody ImageVehicule image) {
        return repository.findById(id).map(vehicule -> {
            vehicule.ajouterImage(image);
            repository.save(vehicule);
            return ResponseEntity.ok(image);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/vehicules/{id}/images/batch")
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

    @DeleteMapping("/vehicules/{vehiculeId}/images/{imageId}")
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

    @PatchMapping("/vehicules/{vehiculeId}/images/{imageId}/principale")
    public ResponseEntity<ImageVehicule> setImagePrincipale(
            @PathVariable Long vehiculeId,
            @PathVariable Long imageId) {
        return repository.findById(vehiculeId).map(vehicule -> {
            for (ImageVehicule img : vehicule.getImages()) {
                img.setEstPrincipale(false);
            }
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

    @PostMapping("/vehicules/{id}/images/upload")
    public ResponseEntity<ImageVehicule> uploadImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "principale", defaultValue = "false") boolean principale,
            HttpServletRequest request) {

        return repository.findById(id).map(vehicule -> {
            String fileName = fileStorageService.storeFile(file);
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String imageUrl = fileStorageService.getFileUrl(fileName, baseUrl);

            ImageVehicule image = new ImageVehicule(imageUrl);
            image.setEstPrincipale(principale);

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

    @PostMapping("/vehicules/{id}/images/upload/batch")
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
