package com.example.med.outil.factory;

import com.example.med.model.commande_et_document.Commande;

/**
 * PATTERN FACTORY METHOD - ConcreteCreator
 *
 * Créateur concret pour les commandes payées au comptant.
 * Implémente la factory method pour créer des CommandeComptant.
 */
public class CreateurCommandeComptant extends CreateurCommande {

    /**
     * Factory method - Crée une commande de type comptant
     * @return Une nouvelle instance de CommandeComptant
     */
    @Override
    protected Commande creer() {
        return new CommandeComptant();
    }
}
