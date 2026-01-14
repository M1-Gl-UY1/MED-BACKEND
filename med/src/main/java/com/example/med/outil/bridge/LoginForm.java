package com.example.med.outil.bridge;

import java.util.Map;

public class LoginForm extends Form{
    public LoginForm(FormRender renderEngine){
        super(renderEngine);
        addField("email", "Email", "email");
        addField("password", "password", "password");
    }
}
