package com.example.med.service;

import com.example.med.model.commande_et_document.Commande;
import com.example.med.model.commande_et_document.LiasseDocuments;
import com.example.med.model.commande_et_document.TypeFormat;
import com.example.med.model.catalogue.Vehicule;
import com.example.med.outil.Builder.DirecteurLiasse;
import com.example.med.outil.Builder.LiasseBuilder;
import com.example.med.outil.Builder.LiasseBuilderHTML;
import com.example.med.outil.Builder.LiasseBuilderPDF;
import com.example.med.outil.command.CommandeSoldeVehicule;
import com.example.med.outil.command.GestionnairesCommandes;
import com.example.med.outil.factory.CreateurCommande;
import com.example.med.outil.templateMethod.CalculCommande;
import com.example.med.repository.CommandeRepository;
import com.example.med.repository.LiasseDocumentsRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CommandeService {
    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private LiasseDocumentsRepository liasseRepository;

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
    @Transactional
    public Commande passerCommande(Commande nouvelleCommande, TypeFormat formatChoisi) {
        
        //  On enregistre  la commande 
        Commande commandeEnregistree = commandeRepository.save(nouvelleCommande);

        // Sélection du Builder 
        LiasseBuilder builder;
        if (formatChoisi == TypeFormat.PDF) {
            builder = new LiasseBuilderPDF();
        } else {
            builder = new LiasseBuilderHTML();
        }

        //  Utilisation du Directeur pour construire la liasse
        
        DirecteurLiasse directeur = new DirecteurLiasse(builder);
        directeur.construireLiasse(commandeEnregistree);

        LiasseDocuments liasseGeneree = builder.getLiasse();

        // Sauvegarde de la liasse 
        liasseRepository.save(liasseGeneree);

        // Association de la liasse à la commande
        commandeEnregistree.setLiasseDocuments(liasseGeneree);
        
        return commandeRepository.save(commandeEnregistree);
    }
}
