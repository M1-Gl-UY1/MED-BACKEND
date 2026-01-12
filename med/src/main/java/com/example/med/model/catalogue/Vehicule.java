package com.example.med.model.catalogue;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Très important pour l'héritage en BDD
@DiscriminatorColumn(name = "type_precis") // Colonne qui dira si c'est une Auto ou un Scooter
public abstract class Vehicule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idVehicule;

    private String nom;
    private String modele;
    private String marque;
    private int annee;
    private double prixBase;

    // On garde tes Enums pour la logique métier
    @Enumerated(EnumType.STRING)
    private TypeEngine engine; 

    @Enumerated(EnumType.STRING)
    private TypeVehicule type;

    // Méthode abstraite que tes sous-classes devront implémenter
    public abstract void afficherDetails();
}