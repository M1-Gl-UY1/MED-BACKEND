package com.example.med.model.catalogue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entité ImageVehicule - Représente une image associée à un véhicule
 *
 * Permet de stocker plusieurs images par véhicule avec un ordre d'affichage.
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "image_vehicule")
public class ImageVehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_image")
    private Long idImage;

    @Column(nullable = false)
    private String url;

    @Column(name = "ordre_affichage")
    private int ordreAffichage = 0;

    @Column(name = "est_principale")
    private boolean estPrincipale = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vehicule")
    @JsonIgnore
    private Vehicule vehicule;

    public ImageVehicule(String url) {
        this.url = url;
    }

    public ImageVehicule(String url, int ordreAffichage, boolean estPrincipale) {
        this.url = url;
        this.ordreAffichage = ordreAffichage;
        this.estPrincipale = estPrincipale;
    }
}
