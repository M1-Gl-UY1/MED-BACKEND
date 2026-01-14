package com.example.med.service.utilisateur;

import com.example.med.model.utilisateur.Client;
import com.example.med.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService{

    private final ClientRepository repository;

    @Override
    public Client trouverParMail(String email) {
        return repository.findByEmail(email).orElseThrow(()-> new RuntimeException("email inexistante"));
    }

    @Override
    public boolean auth(String email, String motDePasse) {
        Client client = trouverParMail(email);
        return BCrypt.checkpw(motDePasse, client.getMotDePasse());
    }
}
