package com.example.med.controller.utilisateur;

import com.example.med.dto.Auth;
import com.example.med.model.utilisateur.Client;
import com.example.med.service.utilisateur.ClientServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientServiceImpl service;

    @PostMapping("/auth")
    public boolean authentification(@RequestBody Auth auth){
        return service.auth(auth.getEmail(),auth.getMotDePasse());
    }
}

