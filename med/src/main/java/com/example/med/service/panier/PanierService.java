package com.example.med.service.panier;

public interface PanierService {
    Boolean ajouterProduit(Long idVehicule, Long idUtilisateur);

    Boolean ajouterOption(Long idPanier, Long idVehicule, Long idOption);
    Boolean retirerProduit(Long idVehicule, Long idUtilisateur);
}
