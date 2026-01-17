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
import com.example.med.service.storage.StorageService;
import com.example.med.service.vehicule.VehiculeService;
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
    private final StorageService storageService;
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

            // Prix et nom - toujours retourner les valeurs originales
            // Le frontend gère l'affichage des réductions avec facteurReduction
            vehiculeData.put("prixBase", v.getPrixBase());
            vehiculeData.put("nom", v.getNom());
            vehiculeData.put("prixOriginal", v.getPrixBase());
            vehiculeData.put("nomOriginal", v.getNom());
            vehiculeData.put("decorated", false);

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

                // Prix et nom - toujours retourner les valeurs originales
                vehiculeData.put("prixBase", v.getPrixBase());
                vehiculeData.put("nom", v.getNom());
                vehiculeData.put("prixOriginal", v.getPrixBase());
                vehiculeData.put("decorated", false);

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
            // Prix et nom - toujours retourner les valeurs originales
            vehiculeData.put("prixBase", v.getPrixBase());
            vehiculeData.put("nom", v.getNom());
            vehiculeData.put("prixOriginal", v.getPrixBase());
            vehiculeData.put("nomOriginal", v.getNom());
            vehiculeData.put("decorated", false);

            return ResponseEntity.ok(vehiculeData);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/vehicules/{id}")
    public ResponseEntity<?> updateVehicule(@PathVariable Long id, @RequestBody Map<String, Object> details) {
        return repository.findById(id).map(existing -> {
            // Informations de base
            if (details.containsKey("nom")) {
                String nom = (String) details.get("nom");
                // Nettoyer le nom des décorations accumulées [PROMO -XX%]
                nom = nom.replaceAll("\\s*\\[PROMO -\\d+%\\]", "").trim();
                existing.setNom(nom);
            }
            if (details.containsKey("marque")) existing.setMarque((String) details.get("marque"));
            if (details.containsKey("model")) existing.setModel((String) details.get("model"));
            if (details.containsKey("prixBase")) {
                Object prix = details.get("prixBase");
                existing.setPrixBase(prix instanceof Number ? ((Number) prix).doubleValue() : 0);
            }
            if (details.containsKey("annee")) {
                Object annee = details.get("annee");
                existing.setAnnee(annee instanceof Number ? ((Number) annee).intValue() : existing.getAnnee());
            }
            if (details.containsKey("description")) existing.setDescription((String) details.get("description"));

            // Caractéristiques techniques
            if (details.containsKey("puissance")) existing.setPuissance((String) details.get("puissance"));
            if (details.containsKey("transmission")) existing.setTransmission((String) details.get("transmission"));
            if (details.containsKey("carburant")) existing.setCarburant((String) details.get("carburant"));
            if (details.containsKey("consommation")) existing.setConsommation((String) details.get("consommation"));
            if (details.containsKey("acceleration")) existing.setAcceleration((String) details.get("acceleration"));
            if (details.containsKey("vitesseMax")) existing.setVitesseMax((String) details.get("vitesseMax"));

            // Couleurs
            if (details.containsKey("couleurs")) {
                @SuppressWarnings("unchecked")
                List<String> couleurs = (List<String>) details.get("couleurs");
                existing.setCouleurs(new ArrayList<>(couleurs));
            }

            // Statuts
            if (details.containsKey("nouveau")) {
                Object nouveau = details.get("nouveau");
                existing.setNouveau(nouveau instanceof Boolean ? (Boolean) nouveau : false);
            }
            if (details.containsKey("solde")) {
                Object solde = details.get("solde");
                existing.setSolde(solde instanceof Boolean ? (Boolean) solde : false);
            }
            if (details.containsKey("facteurReduction")) {
                Object facteur = details.get("facteurReduction");
                existing.setFacteurReduction(facteur instanceof Number ? ((Number) facteur).doubleValue() : 0);
            }

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
    // Recherche full-text
    // ============================================

    /**
     * Recherche full-text sur les véhicules
     * GET /vehicules/search?q=tesla&type=AUTOMOBILE&engine=ELECTRIQUE&marque=Tesla&prixMin=0&prixMax=100000&solde=true
     */
    @GetMapping("/vehicules/search")
    @Transactional(readOnly = true)
    public ResponseEntity<?> searchVehicules(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "engine", required = false) String engine,
            @RequestParam(value = "marque", required = false) String marque,
            @RequestParam(value = "prixMin", required = false) Double prixMin,
            @RequestParam(value = "prixMax", required = false) Double prixMax,
            @RequestParam(value = "solde", required = false) Boolean solde) {

        TypeVehicule typeVehicule = null;
        TypeEngine typeEngine = null;

        if (type != null && !type.isEmpty()) {
            try {
                typeVehicule = TypeVehicule.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException ignored) {}
        }

        if (engine != null && !engine.isEmpty()) {
            try {
                typeEngine = TypeEngine.valueOf(engine.toUpperCase());
            } catch (IllegalArgumentException ignored) {}
        }

        List<Vehicule> vehicules = repository.searchWithFilters(
                query, typeVehicule, typeEngine, marque, prixMin, prixMax, solde
        );

        List<Map<String, Object>> response = new ArrayList<>();
        for (Vehicule v : vehicules) {
            Map<String, Object> vehiculeData = new HashMap<>();
            vehiculeData.put("idVehicule", v.getIdVehicule());
            vehiculeData.put("nom", v.getNom());
            vehiculeData.put("marque", v.getMarque());
            vehiculeData.put("model", v.getModel());
            vehiculeData.put("annee", v.getAnnee());
            vehiculeData.put("type", v.getType());
            vehiculeData.put("engine", v.getEngine());
            vehiculeData.put("prixBase", v.getPrixBase());
            vehiculeData.put("description", v.getDescription());
            vehiculeData.put("images", v.getImages());
            vehiculeData.put("stock", v.getStock());
            vehiculeData.put("solde", v.isSolde());
            vehiculeData.put("facteurReduction", v.getFacteurReduction());
            vehiculeData.put("nouveau", v.isNouveau());
            vehiculeData.put("prixOriginal", v.getPrixBase());
            vehiculeData.put("nomOriginal", v.getNom());
            response.add(vehiculeData);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("total", response.size());
        result.put("query", query);
        result.put("filters", Map.of(
                "type", type != null ? type : "",
                "engine", engine != null ? engine : "",
                "marque", marque != null ? marque : "",
                "prixMin", prixMin != null ? prixMin : "",
                "prixMax", prixMax != null ? prixMax : "",
                "solde", solde != null ? solde : ""
        ));
        result.put("vehicules", response);

        return ResponseEntity.ok(result);
    }

    /**
     * Récupérer les marques disponibles
     */
    @GetMapping("/vehicules/marques")
    @Transactional(readOnly = true)
    public ResponseEntity<List<String>> getMarques() {
        return ResponseEntity.ok(repository.findDistinctMarques());
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
            @RequestParam(value = "principale", defaultValue = "false") boolean principale) {

        return repository.findById(id).map(vehicule -> {
            // storageService.storeFile retourne directement l'URL (Cloudinary ou locale)
            String imageUrl = storageService.storeFile(file);

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
            @RequestParam("files") MultipartFile[] files) {

        return repository.findById(id).map(vehicule -> {
            List<ImageVehicule> uploadedImages = new ArrayList<>();

            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                // storageService.storeFile retourne directement l'URL (Cloudinary ou locale)
                String imageUrl = storageService.storeFile(file);

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
