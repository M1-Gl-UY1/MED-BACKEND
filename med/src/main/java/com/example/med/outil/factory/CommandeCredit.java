package com.example.med.outil.factory;

import com.example.med.model.commande_et_document.Commande;
import com.example.med.model.panier.StatutPanier;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CREDIT")
public class CommandeCredit extends Commande {
    private boolean creditAccepte;

    @Override
    public void valider() {
        if(creditAccepte) {
            setStatut(StatutPanier.VALIDEE);
        }else {
            setStatut(StatutPanier.REFUSEE);
        }
    }

}
