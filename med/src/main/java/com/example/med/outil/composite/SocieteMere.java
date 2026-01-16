package com.example.med.outil.composite;

import com.example.med.model.utilisateur.Societe;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * PATTERN COMPOSITE - Composite
 *
 * Représente une société mère qui peut contenir des filiales.
 * C'est un nœud composite dans la hiérarchie des sociétés.
 *
 * Permet de gérer une arborescence de sociétés où une société mère
 * peut avoir plusieurs filiales (qui peuvent elles-mêmes être des sociétés mères).
 *
 * Exemple d'utilisation:
 * SocieteMere groupe = new SocieteMere();
 * groupe.ajouterFiliale(new Filiale());
 * groupe.ajouterFiliale(new SocieteMere()); // sous-groupe
 */
@Entity
@Getter
@Setter
public class SocieteMere extends Societe {

    @OneToMany
    @JoinTable(
            name = "constituer_ss",
            joinColumns = @JoinColumn(name = "societe_mere_id"),
            inverseJoinColumns = @JoinColumn(name = "societe_fille_id"),
            uniqueConstraints = @UniqueConstraint(
                    columnNames = "societe_fille_id"
            )
    )
    private List<Societe> filiales = new ArrayList<>();

    /**
     * Ajoute une filiale à cette société mère
     * @param filiale La société filiale à ajouter
     * @return true si l'ajout a réussi
     */
    public boolean ajouterFiliale(Societe filiale) {
        if (filiales == null) {
            filiales = new ArrayList<>();
        }
        return filiales.add(filiale);
    }

    /**
     * Retire une filiale de cette société mère
     * @param filiale La société filiale à retirer
     * @return true si le retrait a réussi
     */
    public boolean retirerFiliale(Societe filiale) {
        if (filiales == null) {
            return false;
        }
        return filiales.remove(filiale);
    }

    /**
     * Obtient une filiale par son index
     * @param index L'index de la filiale
     * @return La société filiale à l'index donné
     */
    public Societe obtenirFiliale(int index) {
        if (filiales == null || index < 0 || index >= filiales.size()) {
            throw new IndexOutOfBoundsException("Index invalide: " + index);
        }
        return filiales.get(index);
    }

    /**
     * Retourne le nombre de filiales directes
     * @return Le nombre de filiales
     */
    public int getNombreFiliales() {
        return filiales != null ? filiales.size() : 0;
    }
}
