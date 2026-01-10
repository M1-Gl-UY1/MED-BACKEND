package com.example.med.model.panier;

import com.example.med.model.utilisateur.Utilisateur;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Panier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_panier")
    private Long idPanier;

    private double prix;

    @Enumerated(EnumType.STRING)
    private StatutPanier statut;

    @ManyToOne
    @JoinColumn(name = "id_utilisateur")
    private Utilisateur utilisateur;
}
