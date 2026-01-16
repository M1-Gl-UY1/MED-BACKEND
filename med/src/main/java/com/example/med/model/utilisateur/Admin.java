package com.example.med.model.utilisateur;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Admin extends Utilisateur {

    @Column(name = "derniere_connexion")
    private LocalDateTime derniereConnexion;

    @Column(name = "actif")
    private boolean actif = true;
}
