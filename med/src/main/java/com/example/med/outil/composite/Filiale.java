package com.example.med.outil.composite;

import com.example.med.model.utilisateur.Societe;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * PATTERN COMPOSITE - Leaf (Feuille)
 *
 * Représente une société filiale simple, sans sous-filiales.
 * C'est un élément terminal dans la hiérarchie des sociétés.
 *
 * Une Filiale peut appartenir à une SocieteMere mais
 * ne peut pas contenir d'autres sociétés.
 */
@Entity
@Getter
@Setter
public class Filiale extends Societe {
    // Pas de filiales - c'est une feuille du pattern Composite
}
