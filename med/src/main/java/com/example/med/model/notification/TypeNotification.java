package com.example.med.model.notification;

/**
 * Types de notifications possibles dans le systeme
 */
public enum TypeNotification {
    // Notifications commandes
    NOUVELLE_COMMANDE("Nouvelle commande"),
    COMMANDE_VALIDEE("Commande validee"),
    COMMANDE_LIVREE("Commande livree"),
    COMMANDE_ANNULEE("Commande annulee"),

    // Notifications stock
    STOCK_FAIBLE("Stock faible"),
    RUPTURE_STOCK("Rupture de stock"),

    // Notifications vehicules
    NOUVEAU_VEHICULE("Nouveau vehicule"),
    PRIX_MODIFIE("Prix modifie"),
    PROMOTION("Promotion"),

    // Notifications utilisateurs
    NOUVELLE_INSCRIPTION("Nouvelle inscription"),

    // Notifications systeme
    SYSTEME("Notification systeme");

    private final String label;

    TypeNotification(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
