package com.example.med.model.catalogue;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_option")
    private Long idOption;

    private String description;

    @ManyToMany
    @JoinTable(
            name = "option_incompatible",
            joinColumns = @JoinColumn(name = "id_option"),
            inverseJoinColumns = @JoinColumn( name = "id_option1")
    )
    private List<Option> optionsIncompatible;
}
