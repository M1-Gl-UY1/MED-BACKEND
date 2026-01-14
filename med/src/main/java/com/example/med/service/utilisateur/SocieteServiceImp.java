package com.example.med.service.utilisateur;

import com.example.med.model.utilisateur.Societe;
import com.example.med.repository.SocieteRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocieteServiceImp implements SocieteService
{
    private final SocieteRepository repository;

    @Override
    public Societe trouverParMail(String email) {
        return repository.findByEmail(email).orElseThrow(()-> new RuntimeException("email inexistante"));
    }

    @Override
    public boolean auth(String email, String motDePasse) {
        Societe societe = trouverParMail(email);
        return BCrypt.checkpw(motDePasse, societe.getMotDePasse());
    }
}
