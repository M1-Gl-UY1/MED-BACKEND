package com.example.med.outil.templateMethod;

import com.example.med.model.commande_et_document.Commande;

/**
 * PATTERN TEMPLATE METHOD - AbstractClass
 *
 * Classe abstraite définissant le squelette de l'algorithme de calcul du montant.
 * La méthode calculerMontant() est le "template method" qui définit l'ordre des étapes.
 * Les sous-classes implémentent les étapes variables (calculerTaxes).
 *
 * Structure du pattern:
 * - AbstractClass: CalculCommande (cette classe)
 * - ConcreteClass: CalculCommandeFrance, CalculCommandeCameroun
 * - Template Method: calculerMontant() (FINAL - ne peut pas être redéfinie)
 * - Primitive Operations: calculerPrixBase(), calculerTaxes() (abstract)
 *
 * Exemple d'utilisation:
 * CalculCommande calc = new CalculCommandeFrance();
 * double total = calc.calculerMontant(commande);
 */
public abstract class CalculCommande {

    /**
     * TEMPLATE METHOD - Définit le squelette de l'algorithme (FINAL)
     * Les étapes sont toujours exécutées dans cet ordre:
     * 1. Calculer le prix de base (HT)
     * 2. Ajouter les taxes selon le pays
     *
     * @param commande La commande à calculer
     * @return Le montant total TTC
     */
    public final double calculerMontant(Commande commande) {
        double total = calculerPrixBase(commande);
        total += calculerTaxes(total);
        return total;
    }

    /**
     * Calcule le prix de base de la commande (somme des lignes)
     * Hook method - implémentation par défaut, peut être redéfinie
     *
     * @param commande La commande
     * @return Le prix total HT
     */
    protected double calculerPrixBase(Commande commande) {
        return commande.getLignesCommandes()
                .stream()
                .mapToDouble(ligne -> ligne.getPrixUnitaireHT() * ligne.getQuantite())
                .sum();
    }

    /**
     * Calcule les taxes selon le pays de livraison
     * Primitive operation - DOIT être implémentée par les sous-classes
     *
     * @param montant Le prix HT
     * @return Le montant des taxes
     */
    protected abstract double calculerTaxes(double montant);
}
