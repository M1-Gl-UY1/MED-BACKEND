package com.example.med.model.catalogue;

import com.example.med.outil.observer.Sujet;
import com.example.med.outil.observer.VehiculeObserver;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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
}
