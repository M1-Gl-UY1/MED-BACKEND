package com.example.med.outil.templateMethod;

import com.example.med.model.commande_et_document.Commande;

import static java.lang.Double.sum;

public abstract class CalculCommande {

    public final double calculerMontant(Commande commande) {
        double total = calculerPrixBase(commande);
        total += calculerTaxes(total);
        return total;
    }

    protected double calculerPrixBase(Commande commande) {
        return commande.getLignesCommandes()
                .stream()
                .mapToDouble(ligne -> ligne.getPrixUnitaireHT() * ligne.getQuantite())
                .sum();
    }

    protected abstract double calculerTaxes(double montant);

}