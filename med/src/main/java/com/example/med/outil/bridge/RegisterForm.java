package com.example.med.outil.bridge;

/**
 * PATTERN BRIDGE - RefinedAbstraction
 *
 * Formulaire d'inscription.
 * Peut être rendu en HTML ou en Widget/JSON selon le moteur injecté.
 */
public class RegisterForm extends Form {

    public RegisterForm(FormRender renderEngine) {
        super(renderEngine);
        addField("email", "Adresse Email", "email");
        addField("username", "Nom d'utilisateur", "text");
        addField("password", "Mot de passe", "password");
        addField("confirm", "Confirmer mot de passe", "password");
    }
}
