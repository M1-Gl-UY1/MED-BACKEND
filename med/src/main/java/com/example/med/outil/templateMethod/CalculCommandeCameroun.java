package com.example.med.outil.templateMethod;

import org.springframework.stereotype.Component;

/**
 * PATTERN TEMPLATE METHOD - ConcreteClass
 *
 * Implémentation du calcul pour les livraisons au Cameroun.
 * Applique la TVA camerounaise (19.25%).
 */
@Component("CM")
public class CalculCommandeCameroun extends CalculCommande {

    private static final double TAUX_TVA_CAMEROUN = 0.1925; // 19.25%

    /**
     * Calcule les taxes camerounaises (TVA 19.25%)
     * @param montant Le montant HT
     * @return Le montant des taxes
     */
    @Override
    protected double calculerTaxes(double montant) {
        return montant * TAUX_TVA_CAMEROUN;
    }
}
