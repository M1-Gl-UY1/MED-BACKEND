package com.example.med.outil.factory;

import com.example.med.model.commande_et_document.Commande;

import java.time.LocalDate;

/**
 * PATTERN FACTORY METHOD - Creator (Abstract)
 *
 * Classe abstraite qui déclare la factory method creer().
 * Les sous-classes concrètes implémentent cette méthode pour créer
 * le type de commande approprié (comptant ou crédit).
 *
 * Structure du pattern:
 * - Creator: CreateurCommande (cette classe)
 * - ConcreteCreator: CreateurCommandeComptant, CreateurCommandeCredit
 * - Product: Commande (abstract)
 * - ConcreteProduct: CommandeComptant, CommandeCredit
 *
 * Exemple d'utilisation:
 * CreateurCommande createur = new CreateurCommandeComptant();
 * Commande commande = createur.creerCommande(); // Retourne CommandeComptant
 */
public abstract class CreateurCommande {

    /**
     * Template method qui utilise la factory method.
     * Crée une commande et initialise la date.
     *
     * @return Une nouvelle commande avec la date du jour
     */
    public Commande creerCommande() {
        Commande commande = creer();  // Appel de la factory method
        commande.setDate(LocalDate.now());
        return commande;
    }

    /**
     * Factory Method - À implémenter par les sous-classes.
     * Chaque sous-classe retourne le type de commande approprié.
     *
     * @return Une nouvelle instance du type de commande concret
     */
    protected abstract Commande creer();
}
