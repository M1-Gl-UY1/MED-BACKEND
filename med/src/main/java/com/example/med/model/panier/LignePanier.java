package com.example.med.model.panier;

import com.example.med.model.catalogue.Option;
import com.example.med.model.catalogue.Vehicule;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class LignePanier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ligne_panier")
    private Long idLignePanier;

    @ManyToOne
    @JoinColumn(name = "id_vehicule")
    private Vehicule vehicule;

    @ManyToOne
    @JoinColumn(name = "id_panier")
    private Panier panier;

    @ManyToMany
    @JoinTable(
            name = "choisir_option",
            joinColumns = @JoinColumn(name = "id_ligne_panier"),
            inverseJoinColumns = @JoinColumn(name = "id_option")
    )
    private List<Option> optionsChoisies;
}