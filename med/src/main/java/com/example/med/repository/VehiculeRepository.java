package com.example.med.repository;

import com.example.med.model.catalogue.Vehicule;
import com.example.med.model.catalogue.TypeEngine;
import com.example.med.model.catalogue.TypeVehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {

    /**
     * Recherche full-text sur nom, marque, model et description
     */
    @Query("SELECT v FROM Vehicule v WHERE " +
           "LOWER(v.nom) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(v.marque) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(v.model) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(v.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Vehicule> searchFullText(@Param("query") String query);

    /**
     * Recherche avec filtres combinés
     */
    @Query("SELECT v FROM Vehicule v WHERE " +
           "(:query IS NULL OR :query = '' OR " +
           " LOWER(v.nom) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           " LOWER(v.marque) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           " LOWER(v.model) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           " LOWER(v.description) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:type IS NULL OR v.type = :type) AND " +
           "(:engine IS NULL OR v.engine = :engine) AND " +
           "(:marque IS NULL OR :marque = '' OR v.marque = :marque) AND " +
           "(:prixMin IS NULL OR v.prixBase >= :prixMin) AND " +
           "(:prixMax IS NULL OR v.prixBase <= :prixMax) AND " +
           "(:solde IS NULL OR v.solde = :solde)")
    List<Vehicule> searchWithFilters(
            @Param("query") String query,
            @Param("type") TypeVehicule type,
            @Param("engine") TypeEngine engine,
            @Param("marque") String marque,
            @Param("prixMin") Double prixMin,
            @Param("prixMax") Double prixMax,
            @Param("solde") Boolean solde
    );

    /**
     * Récupérer les marques distinctes
     */
    @Query("SELECT DISTINCT v.marque FROM Vehicule v WHERE v.marque IS NOT NULL ORDER BY v.marque")
    List<String> findDistinctMarques();

    /**
     * Recherche par marque
     */
    List<Vehicule> findByMarqueIgnoreCase(String marque);

    /**
     * Recherche par type de véhicule
     */
    List<Vehicule> findByType(TypeVehicule type);

    /**
     * Recherche par type de moteur
     */
    List<Vehicule> findByEngine(TypeEngine engine);

    /**
     * Véhicules en solde
     */
    List<Vehicule> findBySoldeTrue();

    /**
     * Véhicules nouveaux
     */
    List<Vehicule> findByNouveauTrue();
}