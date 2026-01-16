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

    /**
     * Créer un véhicule
     *
     * NOTE: Le pattern Abstract Factory est démontré dans l'endpoint /vehicules/catalogue/iterate
     * Pour la persistence en base de données, on crée directement des entités Vehicule
     * car les classes produits de l'Abstract Factory (AutomobileElectric, etc.) ne sont
     * pas des entités JPA et ne peuvent pas être persistées directement.
     */
    @PostMapping("/vehicules/")
    public ResponseEntity<Vehicule> createVehicule(@RequestBody VehiculeCreationDTO dto) {

        // Créer une nouvelle entité Vehicule (JPA entity)
        Vehicule vehicule = new Vehicule();

        // Définir le type (AUTOMOBILE ou SCOOTER)
        String type = dto.getType() != null ? dto.getType().toUpperCase() : "AUTOMOBILE";
        if (type.equals("AUTO")) type = "AUTOMOBILE";
        vehicule.setType(type.equals("SCOOTER") ? TypeVehicule.SCOOTER : TypeVehicule.AUTOMOBILE);

        // Définir l'énergie
        String energie = dto.getEnergie() != null ? dto.getEnergie().toUpperCase() : "ESSENCE";
        vehicule.setEngine(energie.equals("ELECTRIQUE") ? TypeEngine.ELECTRIQUE : TypeEngine.ESSENCE);

        // ============================================
        // Configuration des propriétés du véhicule
        // ============================================

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
     *
     * IMPORTANT: On ne modifie PAS l'entité directement pour éviter de persister
     * les changements en base de données. On retourne une Map avec les valeurs décorées.
     */
    @GetMapping(path = "/vehicules")
    public @ResponseBody ResponseEntity<?> findAllCustom() {

        // Récupérer les données de la base
        List<Vehicule> vehicules = repository.findAll();

        // Créer une liste de réponses avec les valeurs décorées
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

                // Valeurs décorées (pour l'affichage uniquement, PAS en BD)
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
     *
     * Cet endpoint démontre l'utilisation du pattern Iterator pour parcourir
     * le catalogue de véhicules sans exposer la structure de données sous-jacente.
     *
     * Structure Iterator utilisée:
     * - Aggregate: Catalogue (interface)
     * - ConcreteAggregate: CatalogueVehicule
     * - Iterator: VehiculeIterator (interface)
     * - ConcreteIterator: CatalogueIterator
     *
     * @param type Filtrer par type (AUTOMOBILE, SCOOTER) - optionnel
     * @param energie Filtrer par énergie (ESSENCE, ELECTRIQUE) - optionnel
     * @return Liste des véhicules filtrés via l'itérateur
     */
    @GetMapping(path = "/vehicules/catalogue/iterate")
    public @ResponseBody ResponseEntity<?> iterateCatalogue(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String energie) {

        // Créer le catalogue (ConcreteAggregate)
        Catalogue catalogue = new CatalogueVehicule();

        // Charger les véhicules depuis la base et les ajouter au catalogue
        List<Vehicule> allVehicules = repository.findAll();
        for (Vehicule v : allVehicules) {
            catalogue.addVehicule(v);
        }

        // Obtenir l'itérateur (ConcreteIterator)
        VehiculeIterator iterator = catalogue.getIterator();

        // Parcourir avec l'itérateur et filtrer
        // IMPORTANT: On utilise des Maps pour ne pas modifier les entités (éviter persistence en BD)
        List<Map<String, Object>> result = new ArrayList<>();

        while (iterator.hasNext()) {
            Vehicule v = (Vehicule) iterator.next();

            // Appliquer les filtres si spécifiés
            boolean matchType = (type == null || v.getType().name().equalsIgnoreCase(type));
            boolean matchEnergie = (energie == null || v.getEngine().name().equalsIgnoreCase(energie));

            if (matchType && matchEnergie) {
                Map<String, Object> vehiculeData = new HashMap<>();

                // Copier les données de base
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

                // Appliquer le Decorator si en solde (sans modifier l'entité)
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

        // Retourner avec métadonnées
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

