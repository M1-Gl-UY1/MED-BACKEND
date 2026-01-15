package com.example.med.controller.utilisateur;

import com.example.med.outil.bridge.Form;
import com.example.med.service.utilisateur.FormServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/form")
@RequiredArgsConstructor
public class FormController {
    private final FormServiceImpl service;

    @GetMapping("/login/{type}")
    public ResponseEntity<String> loginForm(@PathVariable String type){
        Form form = service.getLoginForm(type);
        if (form!=null){
            return ResponseEntity.ok(form.generate());
        }
        return ResponseEntity.badRequest().body("mauvais type");
    }

    @GetMapping("/register/{type}")
    public ResponseEntity<String> registerForm(@PathVariable String type){
        Form form = service.getRegister(type);
        if (form!=null){
            return ResponseEntity.ok(form.generate());
        }
        return ResponseEntity.badRequest().body("mauvais type");

    }
}
