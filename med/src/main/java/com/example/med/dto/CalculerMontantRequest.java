package com.example.med.dto;

import com.example.med.model.commande_et_document.PaysLivraison;

public class CalculerMontantRequest {

    private PaysLivraison paysLivraison;

    public PaysLivraison getPaysLivraison() {
        return paysLivraison;
    }

    public void setPaysLivraison(PaysLivraison paysLivraison) {
        this.paysLivraison = paysLivraison;
    }
}
