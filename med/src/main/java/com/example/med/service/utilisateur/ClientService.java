package com.example.med.service.utilisateur;

import com.example.med.model.utilisateur.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    Client trouverParMail(String email);
    boolean auth(String email, String motDePasse);
}