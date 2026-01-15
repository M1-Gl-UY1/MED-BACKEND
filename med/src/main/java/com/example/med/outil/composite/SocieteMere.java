package com.example.med.outil.composite;
import com.example.med.model.utilisateur.Societe;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class SocieteMere extends Societe{


    @OneToMany
    @JoinTable(
            name = "constituer_ss",
            joinColumns = @JoinColumn(name = "societe_mere_id"),
            inverseJoinColumns = @JoinColumn(name = "societe_fille_id"),
            uniqueConstraints = @UniqueConstraint(
                    columnNames = "societe_fille_id"
            )
    )
    private List<Societe> filiales;
    
    public boolean ajouterFiliale(Societe filiale){
        try {
            filiales.add(filiale);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean retirerFiliale(Societe filiale){
        try {
            filiales.remove(filiale);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Societe ObtenirFiliale(int index){
        return filiales.get(index);
    }
}
