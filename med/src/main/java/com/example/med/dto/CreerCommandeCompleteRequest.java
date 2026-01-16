package com.example.med.dto;

import com.example.med.model.commande_et_document.PaysLivraison;
import com.example.med.model.panier.TypeMethodePaiement;
import lombok.Data;

import java.util.List;

/**
 * DTO complet pour créer une commande avec toutes les informations
 */
@Data
public class CreerCommandeCompleteRequest {

    /**
     * Type de commande: "comptant" ou "credit"
     */
    private String type;

    /**
     * Pays de livraison
     */
    private PaysLivraison paysLivraison;

    /**
     * Adresse de livraison complète
     */
    private String adresseLivraison;

    /**
     * Méthode de paiement
     */
    private TypeMethodePaiement methodePaiement;

    /**
     * Lignes de commande (véhicules commandés)
     */
    private List<LigneCommandeRequest> lignes;

    @Data
    public static class LigneCommandeRequest {
        /**
         * ID du véhicule
         */
        private Long vehiculeId;

        /**
         * Quantité commandée
         */
        private int quantite;

        /**
         * IDs des options sélectionnées
         */
        private List<Long> optionIds;

        /**
         * Couleur sélectionnée
         */
        private String couleur;
    }
}
