package com.example.med.model.commande_et_document;

import com.example.med.model.panier.StatutPanier;
import com.example.med.model.panier.TypeMethodePaiement;
import com.example.med.model.utilisateur.Utilisateur;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Commande {
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

    @OneToMany
    @JoinColumn(name = "id_ligne_commande")
    private List<LigneCommande> lignesCommandes;

    @OneToOne
    @JoinColumn(name = "id_liasse_documents")
    private LiasseDocuments liasseDocuments;

    public void valider() {
        if (statut != StatutPanier.ACTIF) {
            throw new IllegalStateException("Commande non valide");
        }
        this.statut = StatutPanier.VALIDEE;
    }
}
