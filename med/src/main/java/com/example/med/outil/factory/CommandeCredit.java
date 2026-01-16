package com.example.med.outil.factory;

import com.example.med.model.commande_et_document.Commande;
import com.example.med.model.panier.StatutPanier;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * PATTERN FACTORY METHOD - ConcreteProduct
 *
 * Commande avec demande de crédit.
 * La validation dépend de l'acceptation du crédit.
 */
@Entity
@DiscriminatorValue("CREDIT")
public class CommandeCredit extends Commande {

    private boolean creditAccepte;

    /**
     * @return true si le crédit a été accepté
     */
    public boolean isCreditAccepte() {
        return creditAccepte;
    }

    /**
     * Définit si le crédit est accepté ou non
     * @param creditAccepte true si accepté
     */
    public void setCreditAccepte(boolean creditAccepte) {
        this.creditAccepte = creditAccepte;
    }

    /**
     * Valide la commande seulement si le crédit est accepté.
     * Sinon, la commande est refusée.
     */
    @Override
    public void valider() {
        if (creditAccepte) {
            setStatut(StatutPanier.VALIDEE);
        } else {
            setStatut(StatutPanier.REFUSEE);
        }
    }
}
