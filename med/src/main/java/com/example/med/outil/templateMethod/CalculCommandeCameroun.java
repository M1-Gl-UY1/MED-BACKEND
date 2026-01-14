package com.example.med.outil.templateMethod;

public class CalculCommandeCameroun extends CalculCommande{

    @Override
    protected double calculerTaxes(double montant) {
        return montant * 0.19;
    }

}
