package com.example.med.repository;

import com.example.med.model.panier.Panier;
import com.example.med.model.panier.StatutPanier;
import com.example.med.model.utilisateur.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PanierRepository extends JpaRepository<Panier, Long> {

    Panier findByUtilisateurAndStatut(Utilisateur utilisateur, StatutPanier statut);

    // Méthode spécifique pour l'actif
    default Panier getPanierActif(Utilisateur utilisateur) {
        return findByUtilisateurAndStatut(utilisateur, StatutPanier.ACTIF);
    }
}
