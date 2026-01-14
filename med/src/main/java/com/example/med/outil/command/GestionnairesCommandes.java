package com.example.med.outil.command;

import com.example.med.model.commande_et_document.Commande;
import org.springframework.stereotype.Component;

import java.util.Stack;

@Component
public class GestionnairesCommandes {

    private Stack <CommandeAction> historique = new Stack<>();

    public void executerCommande(CommandeAction action) {
        action.executer();
        historique.push(action);
    }

    public void annulerDerniereCommande() {
        if(!historique.isEmpty()) {
            historique.pop().annuler();
        }
    }
}
