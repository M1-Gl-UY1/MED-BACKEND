package com.example.med.model.utilisateur;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utilisateur")
    private long idUtilisateur;
    private String nom;

    @Column(name = "mot_dePasse")
    private String motDePasse;
}