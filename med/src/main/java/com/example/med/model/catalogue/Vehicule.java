package com.example.med.model.catalogue;

import com.example.med.outil.observer.Sujet;
import com.example.med.outil.observer.VehiculeObserver;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Entité Véhicule - Représente un véhicule dans le catalogue
 *
 * Cette classe implémente le pattern OBSERVER (Sujet)
 * pour notifier les observateurs lors des changements de prix.
 *
 * Pour le pattern DECORATOR, utilisez VehiculeDeBase comme wrapper:
 * VehiculeComposant decorated = new DecorateurPromo(new VehiculeDeBase(vehicule));
 */
@Entity
@Data
public class Vehicule implements Sujet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehicule")
    private long idVehicule;

    private String nom;
    private String model;
    private String marque;
    private boolean solde;
    private double facteurReduction;
    private int annee;
    private String imageUrl;

    @OneToMany(mappedBy = "vehicule", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("ordreAffichage ASC")
    private List<ImageVehicule> images = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private TypeEngine engine;

    @Enumerated(EnumType.STRING)
    private TypeVehicule type;

    @Column(name = "prix_base")
    private double prixBase;

    @ManyToOne
    @JoinColumn(name = "id_stock")
    private Stock stock;


    // ============================================
    // PATTERN OBSERVER - Implémentation de Sujet
    // ============================================

    @Transient
    private List<VehiculeObserver> observers = new ArrayList<>();

    /**
     * Modifie le prix et notifie les observateurs (Pattern Observer)
     */
    public void setPrix(double nouveauxPrix) {
        this.prixBase = nouveauxPrix;
        this.notifier();
    }

    @Override
    public void ajouterObserver(VehiculeObserver observer) {
        if (observers == null) {
            observers = new ArrayList<>();
        }
        observers.add(observer);
    }

    @Override
    public void retirerObserver(VehiculeObserver observer) {
        if (observers != null) {
            observers.remove(observer);
        }
    }

    @Override
    public void notifier() {
        if (observers != null) {
            for (VehiculeObserver o : observers) {
                o.update(this);
            }
        }
    }

    // ============================================
    // Gestion des images multiples
    // ============================================

    /**
     * Ajoute une image au véhicule
     */
    public void ajouterImage(ImageVehicule image) {
        if (images == null) {
            images = new ArrayList<>();
        }
        image.setVehicule(this);
        image.setOrdreAffichage(images.size());
        images.add(image);
    }

    /**
     * Retire une image du véhicule
     */
    public void retirerImage(ImageVehicule image) {
        if (images != null) {
            images.remove(image);
            image.setVehicule(null);
        }
    }

    /**
     * Retourne l'image principale (ou la première image)
     */
    public ImageVehicule getImagePrincipale() {
        if (images == null || images.isEmpty()) {
            return null;
        }
        return images.stream()
                .filter(ImageVehicule::isEstPrincipale)
                .findFirst()
                .orElse(images.get(0));
    }

    /**
     * Retourne toutes les URLs des images
     */
    public List<String> getImageUrls() {
        if (images == null) {
            return new ArrayList<>();
        }
        return images.stream()
                .map(ImageVehicule::getUrl)
                .collect(Collectors.toList());
    }
}
