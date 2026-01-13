package com.example.med.model.catalogue;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Vehicule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehicule")
    private long idVehicule;

    private String nom;
    private String model;
    private String marque;
    private int annee;

    @Enumerated(EnumType.STRING)
    private TypeEngine engine;

    @Enumerated(EnumType.STRING)
    private  TypeVehicule type;

    @Column(name = "prix_base")
    private  double prixBase;

    @ManyToOne
    @JoinColumn(name = "id_stock")
    private Stock stock;
}
