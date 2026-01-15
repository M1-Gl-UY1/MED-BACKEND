package com.example.med.dto;

public class VehiculeIteratorDTO {
    private String nom;
    private double prix;
    // Constructeur, Getters et Setters
    public VehiculeIteratorDTO(String nom, double prix) {
        this.nom = nom;
        this.prix = prix;
    }
    public String getNom() { return nom; }
    public double getPrix() { return prix; }
}
