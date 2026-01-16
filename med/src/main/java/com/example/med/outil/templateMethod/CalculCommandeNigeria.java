package com.example.med.outil.templateMethod;

import org.springframework.stereotype.Component;

/**
 * PATTERN TEMPLATE METHOD - ConcreteClass
 *
 * Implémentation du calcul pour les livraisons au Nigeria.
 * Applique la TVA nigériane (7.5%).
 */
@Component("NG")
public class CalculCommandeNigeria extends CalculCommande {

    private static final double TAUX_TVA_NIGERIA = 0.075; // 7.5%

    /**
     * Calcule les taxes nigérianes (VAT 7.5%)
     * @param montant Le montant HT
     * @return Le montant des taxes
     */
    @Override
    protected double calculerTaxes(double montant) {
        return montant * TAUX_TVA_NIGERIA;
    }
}
