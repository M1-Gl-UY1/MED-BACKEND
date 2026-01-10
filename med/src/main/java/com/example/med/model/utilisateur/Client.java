package com.example.med.model.utilisateur;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Client extends Utilisateur{

    private String nom;
    private String prenom;
    private String sexe;

    @Column(name = "date_n")
    private LocalDate dateNaissance;
}


