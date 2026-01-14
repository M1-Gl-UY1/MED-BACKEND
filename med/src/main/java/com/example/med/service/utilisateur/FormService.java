package com.example.med.service.utilisateur;

import com.example.med.outil.bridge.Form;

public interface FormService {
    Form getLoginForm(String type);
    Form getRegister(String type);
}
