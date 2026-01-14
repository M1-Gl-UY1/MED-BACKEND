package com.example.med.service.utilisateur;

import com.example.med.model.utilisateur.Client;
import com.example.med.model.utilisateur.Societe;

import java.util.List;

public interface SocieteService {
    Societe trouverParMail(String email);
    boolean auth(String email, String motDePasse);
}
