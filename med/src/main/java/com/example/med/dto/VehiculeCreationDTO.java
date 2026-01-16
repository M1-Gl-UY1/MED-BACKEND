package com.example.med.dto;

import lombok.Data;
import java.util.List;

@Data
public class VehiculeCreationDTO {
    // Type de véhicule (pour Abstract Factory)
    private String energie;     // ESSENCE ou ELECTRIQUE
    private String type;        // AUTOMOBILE ou SCOOTER

    // Informations de base
    private String nom;
    private String marque;
    private String model;
    private Integer annee;
    private Double prixBase;
    private String description;

    // Caractéristiques techniques
    private String puissance;
    private String transmission;
    private String carburant;
    private String consommation;
    private String acceleration;
    private String vitesseMax;

    // Couleurs disponibles
    private List<String> couleurs;

    // Options (IDs des options à associer)
    private List<Long> optionIds;

    // Stock
    private Integer quantiteStock;

    // Statuts
    private Boolean nouveau;
    private Boolean solde;
    private Double facteurReduction;

    // Images (URLs)
    private List<String> imageUrls;
}