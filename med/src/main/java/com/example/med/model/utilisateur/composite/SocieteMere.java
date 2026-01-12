package com.example.med.model.utilisateur.composite;
import lombok.Data;
import java.util.List;

@Data
public class SocieteMere {
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
