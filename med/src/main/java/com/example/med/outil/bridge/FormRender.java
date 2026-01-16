package com.example.med.outil.bridge;

import java.util.Map;

/**
 * PATTERN BRIDGE - Implementor (Interface)
 *
 * Interface définissant le contrat pour les moteurs de rendu.
 * Les implémentations concrètes (HtmlRender, WidgetRender)
 * produisent différents formats de sortie.
 */
public interface FormRender {

    /**
     * Rend les données du formulaire dans le format approprié
     * @param data Les champs du formulaire (nom -> propriétés)
     * @return Le formulaire rendu
     */
    String render(Map<String, Map<String, String>> data);
}
