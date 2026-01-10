package com.example.med.model.commande_et_document;

import com.example.med.model.catalogue.Option;
import com.example.med.model.catalogue.Vehicule;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class LigneCommande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ligne_commande")
    private Long idLigneCommande;

    @Column(name = "prix_unitaire_HT")
    private double prixUnitaireHT;

    private long quantite;

    @Column(name = "taux_TVA")
    private double tauxTVA;

    @ManyToOne
    @JoinColumn(name = "id_vehicule")
    private Vehicule vehicule;

    @ManyToOne
    @JoinColumn(name = "id_commande")
    private Commande commande;

    @ManyToMany
    @JoinTable(
        name = "avoir_option",
        joinColumns = @JoinColumn(name = "id_ligne_commande"),
        inverseJoinColumns = @JoinColumn(name = "id_option")
    )
    private List<Option> optionsAchetees;
}
