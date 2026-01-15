package com.example.med.service.utilisateur;

import com.example.med.outil.bridge.*;
import org.springframework.stereotype.Service;

@Service
public class FormServiceImpl implements FormService{
    @Override
    public Form getLoginForm(String type) {
        return switch (type) {
            case "html" -> new LoginForm(new HtmlRender());
            case "widget" -> new LoginForm(new WidgetRender());
            default -> null;
        };
    }

    @Override
    public Form getRegister(String type) {
        return switch (type) {
            case "html" -> new RegisterForm(new HtmlRender());
            case "widget" -> new RegisterForm(new WidgetRender());
            default -> null;
        };
    }
}
