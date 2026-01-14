package com.example.med.outil.templateMethod;

public class CalculCommandeFrance extends CalculCommande {

    @Override
    protected double calculerTaxes(double montant) {
        return montant * 0.20;
    }

}
