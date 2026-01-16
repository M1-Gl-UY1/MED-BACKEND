package com.example.med.outil.Builder;

import com.example.med.model.commande_et_document.Commande;

/**
 * PATTERN BUILDER - Director
 *
 * Le Directeur définit l'ordre d'appel des étapes de construction.
 * Il travaille avec n'importe quel builder (PDF ou HTML) via l'interface LiasseBuilder.
 *
 * Responsabilités:
 * - Définir la séquence de construction
 * - Garantir que tous les documents obligatoires sont créés
 * - Isoler le code client des détails de construction
 *
 * Exemple d'utilisation:
 * LiasseBuilder builder = new LiasseBuilderPDF();
 * DirecteurLiasse directeur = new DirecteurLiasse(builder);
 * directeur.construireLiasse(commande);
 * LiasseDocuments liasse = builder.getLiasse();
 */
public class DirecteurLiasse {

    private LiasseBuilder builder;

    /**
     * Constructeur avec injection du builder
     * @param builder Le builder à utiliser (PDF ou HTML)
     */
    public DirecteurLiasse(LiasseBuilder builder) {
        this.builder = builder;
    }

    /**
     * Change le builder utilisé
     * @param builder Le nouveau builder
     */
    public void setBuilder(LiasseBuilder builder) {
        this.builder = builder;
    }

    /**
     * Construit la liasse complète de documents pour une commande.
     * Crée les 3 documents obligatoires dans l'ordre:
     * 1. Bon de commande
     * 2. Certificat de cession
     * 3. Demande d'immatriculation
     *
     * @param commande La commande pour laquelle créer la liasse
     */
    public void construireLiasse(Commande commande) {
        // Initialiser la liasse avec la commande
        builder.creerNouvelleLiasse(commande);

        // Construire les 3 documents obligatoires
        builder.construireBonCommande("Bon de Commande Officiel");
        builder.construireCertificatCession("Certificat de Cession de Véhicule");
        builder.construireDemandeImmatriculation("Demande d'Immatriculation Préfecture");
    }

    /**
     * Construit uniquement le bon de commande
     * @param commande La commande
     */
    public void construireBonCommandeSeul(Commande commande) {
        builder.creerNouvelleLiasse(commande);
        builder.construireBonCommande("Bon de Commande");
    }

    /**
     * Construit la liasse complète sans commande (version simplifiée pour API).
     * Utilise les informations client et véhicule directement.
     *
     * @param client Nom du client
     * @param vehicule Nom du véhicule
     */
    public void construireLiasse(String client, String vehicule) {
        // Initialiser la liasse sans commande
        builder.creerNouvelleLiasse(null);

        // Construire les 3 documents obligatoires
        builder.construireBonCommande("Bon de Commande - " + client + " - " + vehicule);
        builder.construireCertificatCession("Certificat de Cession - " + vehicule);
        builder.construireDemandeImmatriculation("Demande d'Immatriculation - " + vehicule);
    }
}
