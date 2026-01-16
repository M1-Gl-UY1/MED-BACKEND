package com.example.med.outil.command;

import org.springframework.stereotype.Component;

import java.util.Stack;

/**
 * PATTERN COMMAND - Invoker
 *
 * Le gestionnaire de commandes (Invoker) qui:
 * - Exécute les commandes
 * - Maintient un historique pour l'annulation (undo)
 *
 * Utilise une Stack pour gérer l'historique LIFO (Last In First Out).
 */
@Component
public class GestionnairesCommandes {

    // Historique des commandes exécutées (pour undo)
    private Stack<CommandeAction> historique = new Stack<>();

    /**
     * Exécute une commande et l'ajoute à l'historique
     * @param action La commande à exécuter
     */
    public void executerCommande(CommandeAction action) {
        action.executer();
        historique.push(action);
    }

    /**
     * Annule la dernière commande exécutée (undo)
     * Retire la commande de l'historique et l'annule
     */
    public void annulerDerniereCommande() {
        if (!historique.isEmpty()) {
            historique.pop().annuler();
        }
    }

    /**
     * Vérifie s'il y a des commandes à annuler
     * @return true si l'historique n'est pas vide
     */
    public boolean peutAnnuler() {
        return !historique.isEmpty();
    }

    /**
     * Retourne le nombre de commandes dans l'historique
     * @return Le nombre de commandes
     */
    public int getTailleHistorique() {
        return historique.size();
    }
}
