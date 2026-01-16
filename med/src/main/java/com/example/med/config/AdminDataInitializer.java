package com.example.med.config;

import com.example.med.model.utilisateur.Admin;
import com.example.med.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminDataInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;

    private static final String ADMIN_EMAIL = "admin-med@gmail.com";
    private static final String ADMIN_PASSWORD = "12345678";

    @Override
    public void run(String... args) {
        if (!adminRepository.existsByEmail(ADMIN_EMAIL)) {
            Admin admin = new Admin();
            admin.setEmail(ADMIN_EMAIL);
            admin.setNom("Administrateur");
            admin.setMotDePasse(BCrypt.hashpw(ADMIN_PASSWORD, BCrypt.gensalt()));
            admin.setActif(true);

            adminRepository.save(admin);
            log.info("Compte administrateur cree: {}", ADMIN_EMAIL);
        } else {
            log.info("Compte administrateur existe deja: {}", ADMIN_EMAIL);
        }
    }
}
