package com.example.med.outil.factory;

import com.example.med.model.commande_et_document.Commande;
import com.example.med.model.panier.StatutPanier;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("COMPTANT")
public class CommandeComptant extends Commande {

    @Override
    public void valider() {
        setStatut(StatutPanier.VALIDEE);
    }

}
