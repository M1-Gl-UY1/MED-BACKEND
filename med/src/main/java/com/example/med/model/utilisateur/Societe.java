package com.example.med.model.utilisateur;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Societe extends Utilisateur {

    private String nom;

    @Column(name = "numero_taxe")
    private String numeroTaxe;

    @ManyToOne
    @JoinColumn(name = "id_utilisateur_parent")
    private List<Societe> societeMere;

}
