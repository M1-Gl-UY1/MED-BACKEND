package com.example.med.model.catalogue;

import com.example.med.outil.decorator.VehiculeComposant;
import com.example.med.outil.observer.Sujet;
import com.example.med.outil.observer.VehiculeObserver;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Vehicule implements VehiculeComposant, Sujet {
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


    // utile pour le design pattern decorator
    public double getPrix(double facteurReduction) {
        return prixBase;
    }


    // utile pour le design pattern observer
    public void setPrix(double nouveauxPrix){
        this.prixBase=nouveauxPrix;
        this.notifier();
    }

    @Transient
    private List<VehiculeObserver> observers = new ArrayList<>();

    @Override
    public void ajouterObserver(VehiculeObserver observer) {
        observers.add(observer);
    }

    @Override
    public void retirerObserver(VehiculeObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifier() {
        for (VehiculeObserver o : observers) {
            o.update(this);
        }
    }
}
