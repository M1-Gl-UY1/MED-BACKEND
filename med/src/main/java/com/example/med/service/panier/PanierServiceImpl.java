package com.example.med.service.panier;

import com.example.med.model.catalogue.Option;
import com.example.med.model.catalogue.Vehicule;
import com.example.med.model.panier.LignePanier;
import com.example.med.model.panier.Panier;
import com.example.med.model.panier.StatutPanier;
import com.example.med.model.utilisateur.Utilisateur;
import com.example.med.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PanierServiceImpl implements PanierService{

    private final PanierRepository panierRepository;
    private final VehiculeRepository vehiculeRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final LignePanierRepository lignePanierRepository;
    private final OptionRepository optionRepository;

    @Override
    public Boolean ajouterProduit(Long idVehicule, Long idUtilisateur) {
        Optional<Utilisateur> tmpUtilisateur = utilisateurRepository.findById(idUtilisateur);
        Optional<Vehicule> tmpVehicule = vehiculeRepository.findById(idVehicule);

        Panier panier;
        Utilisateur utilisateur;
        Vehicule vehicule;

        if (tmpVehicule.isPresent()){
            vehicule = tmpVehicule.get();
        }else {
            return false;
        }

        if (tmpUtilisateur.isPresent()){
            utilisateur = tmpUtilisateur.get();
            Optional<Panier> tmpPanier = Optional.ofNullable(panierRepository.getPanierActif(utilisateur));

            if (tmpPanier.isPresent()){
                panier=tmpPanier.get();
            }else {
                panier = new Panier();
                panier.setUtilisateur(utilisateur);
                panier.setStatut(StatutPanier.ACTIF);
                panier.setPrix(0);
                panierRepository.save(panier);
            }

            LignePanier lignePanier = new LignePanier();
            lignePanier.setVehicule(vehicule);
            lignePanier.setPanier(panier);
            lignePanierRepository.save(lignePanier);
            return true;
        }
        return false;
    }

    @Override
    public Boolean ajouterOption(Long idVehicule, Long idPanier, Long idOption) {
        Vehicule vehicule;
        Panier panier;
        LignePanier lignePanier;
        Option option;

        Optional<Option> tmpOption = optionRepository.findById(idOption);
        Optional<Vehicule> tmpVehicule = vehiculeRepository.findById(idVehicule);
        Optional<Panier> tmpPanier = panierRepository.findById(idPanier);

        if (tmpVehicule.isPresent()){vehicule=tmpVehicule.get();} else {return false;}
        if (tmpPanier.isPresent()){panier=tmpPanier.get();}else {return false;}
        if (tmpOption.isPresent()){option=tmpOption.get();}else {return false;}

        Optional<LignePanier> tmpLignePanier = Optional.ofNullable(lignePanierRepository.findByPanierAndVehicule(panier, vehicule));

        if (tmpLignePanier.isPresent()){lignePanier=tmpLignePanier.get();}else {return false;}

        List<Option> options = lignePanier.getOptionsChoisies();
        options.add(option);

        lignePanier.setOptionsChoisies(options);

        return true;
    }

    @Override
    public Boolean retirerProduit(Long idVehicule, Long idUtilisateur) {

        Optional<Utilisateur> tmpUtilisateur = utilisateurRepository.findById(idUtilisateur);
        Optional<Vehicule> tmpVehicule = vehiculeRepository.findById(idVehicule);

        Panier panier;
        Utilisateur utilisateur;
        Vehicule vehicule;
        LignePanier lignePanier;

        if (tmpVehicule.isPresent()){
            vehicule = tmpVehicule.get();
        }else {
            return false;
        }

        if (tmpUtilisateur.isPresent()){
            utilisateur = tmpUtilisateur.get();
            Optional<Panier> tmpPanier = Optional.ofNullable(panierRepository.getPanierActif(utilisateur));

            if (tmpPanier.isPresent()) {
                panier = tmpPanier.get();
            }else {
                return false;
            }

            Optional<LignePanier> tmpLignePanier = Optional.ofNullable(lignePanierRepository.findByPanierAndVehicule(panier, vehicule));
            if (tmpLignePanier.isPresent()){lignePanier=tmpLignePanier.get();}else {return false;}

            lignePanierRepository.delete(lignePanier);
            return true;
        }
        return false;
    }
}