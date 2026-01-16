package com.example.med.model.catalogue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_option")
    private Long idOption;

    private String nom;
    private String description;
    private double prix;

    @Enumerated(EnumType.STRING)
    private CategorieOption categorie;

    @ManyToMany
    @JoinTable(
            name = "option_incompatible",
            joinColumns = @JoinColumn(name = "id_option"),
            inverseJoinColumns = @JoinColumn(name = "id_option1")
    )
    @JsonIgnore  // Éviter la récursion infinie lors de la sérialisation JSON
    private List<Option> optionsIncompatible = new ArrayList<>();
}
