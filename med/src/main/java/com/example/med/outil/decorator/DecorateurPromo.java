package com.example.med.outil.decorator;

/**
 * PATTERN DECORATOR - ConcreteDecorator
 *
 * Décorateur qui ajoute une promotion (-10%) au véhicule.
 * Peut être chaîné avec d'autres décorateurs.
 *
 * Exemple d'utilisation:
 * VehiculeComposant v = new DecorateurPromo(new VehiculeDeBase(vehicule));
 * // ou chaîné:
 * VehiculeComposant v = new DecorateurPromo(new DecorateurDestock(new VehiculeDeBase(vehicule)));
 */
public class DecorateurPromo extends DecorateurVehicule {

    private static final double TAUX_REDUCTION = 0.10; // 10% de réduction

    public DecorateurPromo(VehiculeComposant composant) {
        super(composant);
    }

    @Override
    public String getNom() {
        return composant.getNom() + " [PROMO -10%]";
    }

    @Override
    public double getPrix() {
        // Applique 10% de réduction sur le prix du composant wrappé
        return composant.getPrix() * (1 - TAUX_REDUCTION);
    }

    @Override
    public String getDescription() {
        return composant.getDescription() + " | Promotion: -10%";
    }

    @Override
    public void afficher() {
        System.out.println("=== Véhicule en PROMOTION ===");
        System.out.println("Nom: " + getNom());
        System.out.println("Description: " + getDescription());
        System.out.println("Prix original: " + composant.getPrix() + " €");
        System.out.println("Prix promo: " + getPrix() + " €");
        System.out.println("Économie: " + (composant.getPrix() * TAUX_REDUCTION) + " €");
    }

    /**
     * Retourne le taux de réduction appliqué
     */
    public double getTauxReduction() {
        return TAUX_REDUCTION;
    }
}
