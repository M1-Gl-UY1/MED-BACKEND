package com.example.med.outil.templateMethod;

import org.springframework.stereotype.Component;

/**
 * PATTERN TEMPLATE METHOD - ConcreteClass
 *
 * Implémentation du calcul pour les livraisons aux États-Unis.
 * Applique la taxe de vente américaine moyenne (8%).
 */
@Component("US")
public class CalculCommandeUSA extends CalculCommande {

    private static final double TAUX_TAXE_USA = 0.08; // 8% (moyenne nationale)

    /**
     * Calcule les taxes américaines (Sales Tax 8%)
     * @param montant Le montant HT
     * @return Le montant des taxes
     */
    @Override
    protected double calculerTaxes(double montant) {
        return montant * TAUX_TAXE_USA;
    }
}
