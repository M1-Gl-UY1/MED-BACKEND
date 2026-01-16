package com.example.med.model.utilisateur;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * PATTERN COMPOSITE - Component
 *
 * Classe représentant une société (composant de base).
 * Hérite de Utilisateur et sert de classe parent pour Filiale et SocieteMere.
 *
 * Structure du pattern:
 * - Component: Societe (cette classe)
 * - Leaf: Filiale (société simple sans filiales)
 * - Composite: SocieteMere (société avec des filiales)
 *
 * Le pattern Composite permet de traiter uniformément
 * les sociétés simples et les groupes de sociétés.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Societe extends Utilisateur {

    private String nom;

    @Column(name = "numero_taxe")
    private String numeroTaxe;
}
