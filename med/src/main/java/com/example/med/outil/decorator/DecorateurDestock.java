package com.example.med.outil.decorator;

/**
 * PATTERN DECORATOR - ConcreteDecorator
 *
 * Décorateur qui marque un véhicule comme étant en déstockage
 * avec une réduction de 20%.
 * Peut être chaîné avec d'autres décorateurs.
 *
 * Exemple d'utilisation:
 * VehiculeComposant v = new DecorateurDestock(new VehiculeDeBase(vehicule));
 * // ou chaîné avec promo (cumul des réductions):
 * VehiculeComposant v = new DecorateurPromo(new DecorateurDestock(new VehiculeDeBase(vehicule)));
 */
public class DecorateurDestock extends DecorateurVehicule {

    private static final double TAUX_DESTOCK = 0.20; // 20% de réduction déstockage

    public DecorateurDestock(VehiculeComposant composant) {
        super(composant);
    }

    @Override
    public String getNom() {
        return composant.getNom() + " [DESTOCKAGE -20%]";
    }

    @Override
    public double getPrix() {
        // Applique 20% de réduction pour le déstockage
        return composant.getPrix() * (1 - TAUX_DESTOCK);
    }

    @Override
    public String getDescription() {
        return composant.getDescription() + " | DESTOCKAGE: -20%";
    }

    @Override
    public void afficher() {
        System.out.println("=== Véhicule en DESTOCKAGE ===");
        System.out.println("Nom: " + getNom());
        System.out.println("Description: " + getDescription());
        System.out.println("Prix avant déstockage: " + composant.getPrix() + " €");
        System.out.println("Prix déstockage: " + getPrix() + " €");
        System.out.println("Économie: " + (composant.getPrix() * TAUX_DESTOCK) + " €");
    }

    /**
     * Retourne le taux de déstockage appliqué
     */
    public double getTauxDestock() {
        return TAUX_DESTOCK;
    }
}
