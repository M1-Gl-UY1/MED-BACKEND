package com.example.med.model.commande_et_document;

import com.example.med.model.panier.StatutPanier;
import com.example.med.model.panier.TypeMethodePaiement;
import com.example.med.model.utilisateur.Utilisateur;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_commande")
    private Long idCommande;

    private double total;

    @Column(name = "type_paiement")
    private TypeMethodePaiement typePaiement;

    @Enumerated(EnumType.STRING)
    private StatutPanier statut;

    @Column(name = "pays_livraison")
    private PaysLivraison paysLivraison;

    private double taxe;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "id_utilisateur")
    private Utilisateur utilisateur;

    @OneToOne
    @JoinColumn(name = "id_liasse_documents")
    private LiasseDocuments liasseDocuments;
}
