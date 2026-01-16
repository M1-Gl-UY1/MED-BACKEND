package com.example.med.outil.Builder;

import com.example.med.model.commande_et_document.Commande;
import com.example.med.model.commande_et_document.Document;
import com.example.med.model.commande_et_document.LiasseDocuments;

/**
 * PATTERN BUILDER - Builder (Abstract)
 *
 * Classe abstraite définissant les étapes de construction d'une liasse de documents.
 * Chaque builder concret (LiasseBuilderPDF, LiasseBuilderHTML) implémente ces étapes
 * pour créer des documents dans un format spécifique.
 *
 * Structure du pattern:
 * - Builder: LiasseBuilder (cette classe)
 * - ConcreteBuilder: LiasseBuilderPDF, LiasseBuilderHTML
 * - Director: DirecteurLiasse
 * - Product: LiasseDocuments contenant des Document
 *
 * Exemple d'utilisation:
 * LiasseBuilder builder = new LiasseBuilderPDF();
 * DirecteurLiasse directeur = new DirecteurLiasse(builder);
 * directeur.construireLiasse(commande);
 * LiasseDocuments liasse = builder.getLiasse();
 */
public abstract class LiasseBuilder {

    protected LiasseDocuments liasse;
    protected Commande commandeRef;

    /**
     * Initialise une nouvelle liasse vierge pour la commande donnée
     * @param commande La commande associée à la liasse
     */
    public void creerNouvelleLiasse(Commande commande) {
        this.liasse = LiasseDocuments.creerNouvelleInstance();
        this.commandeRef = commande;
    }

    /**
     * Retourne la liasse construite
     * @return La liasse de documents
     */
    public LiasseDocuments getLiasse() {
        return liasse;
    }

    /**
     * Construit le bon de commande
     * @param contenu Le contenu/titre du document
     */
    public abstract void construireBonCommande(String contenu);

    /**
     * Construit le certificat de cession
     * @param contenu Le contenu/titre du document
     */
    public abstract void construireCertificatCession(String contenu);

    /**
     * Construit la demande d'immatriculation
     * @param contenu Le contenu/titre du document
     */
    public abstract void construireDemandeImmatriculation(String contenu);

    /**
     * Méthode factory pour créer un document dans le format approprié
     * @return Un nouveau document vide dans le format du builder
     */
    public abstract Document creerDocument();
}
