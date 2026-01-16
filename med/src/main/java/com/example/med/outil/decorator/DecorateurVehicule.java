package com.example.med.outil.decorator;

/**
 * PATTERN DECORATOR - Decorator (Abstract)
 *
 * Classe abstraite qui implémente VehiculeComposant et maintient
 * une référence vers un objet VehiculeComposant.
 *
 * Cette classe permet le chaînage des décorateurs:
 * new DecorateurPromo(new DecorateurDestock(new VehiculeDeBase(vehicule)))
 *
 * Chaque décorateur délègue les appels au composant wrappé
 * et peut ajouter son propre comportement avant/après.
 */
public abstract class DecorateurVehicule implements VehiculeComposant {

    protected final VehiculeComposant composant;

    /**
     * Constructeur qui accepte n'importe quel VehiculeComposant
     * permettant ainsi le chaînage des décorateurs
     */
    public DecorateurVehicule(VehiculeComposant composant) {
        if (composant == null) {
            throw new IllegalArgumentException("Le composant ne peut pas être null");
        }
        this.composant = composant;
    }

    /**
     * Délégation par défaut - les sous-classes peuvent override
     */
    @Override
    public String getNom() {
        return composant.getNom();
    }

    /**
     * Délégation par défaut - les sous-classes peuvent override
     */
    @Override
    public double getPrix() {
        return composant.getPrix();
    }

    /**
     * Délégation par défaut - les sous-classes peuvent override
     */
    @Override
    public String getDescription() {
        return composant.getDescription();
    }

    /**
     * Délégation par défaut - les sous-classes peuvent override
     */
    @Override
    public void afficher() {
        composant.afficher();
    }

    /**
     * Permet d'accéder au composant wrappé
     */
    public VehiculeComposant getComposant() {
        return composant;
    }
}
