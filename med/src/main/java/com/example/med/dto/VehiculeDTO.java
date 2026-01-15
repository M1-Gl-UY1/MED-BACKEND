package com.example.med.dto;

import lombok.Data;

@Data
public class VehiculeDTO {
    private Long idVehicule;
    private String nom;         // Sera la description décorée
    private double prix;        // Sera le prix décoré

    // Les autres champs statiques
    private String marque;
    private String model;
    private int annee;
    private String type;
    private String engine;
    private String imageUrl;
    private boolean solde;
    // Ajoutez stock si besoin
}