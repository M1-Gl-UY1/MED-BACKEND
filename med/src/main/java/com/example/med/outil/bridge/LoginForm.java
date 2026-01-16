package com.example.med.outil.bridge;

/**
 * PATTERN BRIDGE - RefinedAbstraction
 *
 * Formulaire de connexion.
 * Peut être rendu en HTML ou en Widget/JSON selon le moteur injecté.
 */
public class LoginForm extends Form {

    public LoginForm(FormRender renderEngine) {
        super(renderEngine);
        addField("email", "Email", "email");
        addField("password", "Mot de passe", "password");
    }
}
