package com.example.med.outil.factory;

import com.example.med.model.commande_et_document.Commande;

/**
 * PATTERN FACTORY METHOD - ConcreteCreator
 *
 * Créateur concret pour les commandes avec demande de crédit.
 * Implémente la factory method pour créer des CommandeCredit.
 */
public class CreateurCommandeCredit extends CreateurCommande {

    /**
     * Factory method - Crée une commande de type crédit
     * @return Une nouvelle instance de CommandeCredit
     */
    @Override
    protected Commande creer() {
        return new CommandeCredit();
    }
}
