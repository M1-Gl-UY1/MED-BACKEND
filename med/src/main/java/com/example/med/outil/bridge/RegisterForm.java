package com.example.med.outil.bridge;

public class RegisterForm extends Form{
    public RegisterForm(FormRender renderEngine){
        super(renderEngine);
        addField("email", "Adresse Email", "email");
        addField("username", "Nom d'utilisateur", "text");
        addField("password", "Mot de passe", "password");
        addField("confirm", "Confirmer mot de passe", "password");
    }
}
