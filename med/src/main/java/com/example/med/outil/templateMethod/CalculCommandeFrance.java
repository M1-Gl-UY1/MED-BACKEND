package com.example.med.outil.templateMethod;

/**
 * PATTERN TEMPLATE METHOD - ConcreteClass
 *
 * Implémentation du calcul pour les livraisons en France.
 * Applique la TVA française (20%).
 */
public class CalculCommandeFrance extends CalculCommande {

    private static final double TAUX_TVA_FRANCE = 0.20; // 20%

    /**
     * Calcule les taxes françaises (TVA 20%)
     * @param montant Le montant HT
     * @return Le montant des taxes
     */
    @Override
    protected double calculerTaxes(double montant) {
        return montant * TAUX_TVA_FRANCE;
    }
}
