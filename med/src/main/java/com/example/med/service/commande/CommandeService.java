package com.example.med.service.commande;

import com.example.med.model.commande_et_document.Commande;
import com.example.med.model.catalogue.Vehicule;
import com.example.med.outil.command.CommandeSoldeVehicule;
import com.example.med.outil.command.GestionnairesCommandes;
import com.example.med.outil.factory.CreateurCommande;
import com.example.med.outil.templateMethod.CalculCommande;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CommandeService {

    private final Map<String, CreateurCommande> createursCommande;
    private final Map<String, CalculCommande> calculateurs;
    private final GestionnairesCommandes gestionnairesCommandes;

    public CommandeService(
            Map<String, CreateurCommande> createursCommande,
            Map<String, CalculCommande> calculateurs,
            GestionnairesCommandes gestionnairesCommandes) {
        this.createursCommande = createursCommande;
        this.calculateurs = calculateurs;
        this.gestionnairesCommandes = gestionnairesCommandes;
    }

    public Commande creerCommande(String typeCommande) {
        CreateurCommande createur = createursCommande.get(typeCommande);
        if (createur == null) {
            throw new IllegalArgumentException("Type de commande non supporté : " + typeCommande);
        }
        return createur.creerCommande();
    }

    public double calculerMontant(Commande commande, String codePays) {
        CalculCommande calculateur = calculateurs.get(codePays);
        if (calculateur == null) {
            throw new IllegalArgumentException("Pays non supporté : " + codePays);
        }
        return calculateur.calculerMontant(commande);
    }

    public void validerCommande(Commande commande) {
        commande.valider();
    }

    public void appliquerSoldeVehicule(Vehicule vehicule, double tauxReduction) {
        gestionnairesCommandes.executerCommande(
                new CommandeSoldeVehicule(vehicule, tauxReduction)
        );
    }

    public void annulerDerniereAction() {
        gestionnairesCommandes.annulerDerniereCommande();
    }
}
