package com.example.med.outil.factory;

import com.example.med.model.commande_et_document.Commande;
import com.example.med.model.panier.StatutPanier;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * PATTERN FACTORY METHOD - ConcreteProduct
 *
 * Commande payée au comptant.
 * La validation est immédiate car le paiement est direct.
 */
@Entity
@DiscriminatorValue("COMPTANT")
public class CommandeComptant extends Commande {

    /**
     * Valide la commande immédiatement car le paiement est au comptant
     */
    @Override
    public void valider() {
        setStatut(StatutPanier.VALIDEE);
    }
}
