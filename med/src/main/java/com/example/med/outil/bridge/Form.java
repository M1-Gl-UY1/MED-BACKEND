package com.example.med.outil.bridge;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * PATTERN BRIDGE - Abstraction
 *
 * Classe abstraite représentant un formulaire.
 * Sépare l'abstraction (Form) de son implémentation (FormRender).
 *
 * Structure du pattern:
 * - Abstraction: Form (cette classe)
 * - RefinedAbstraction: LoginForm, RegisterForm
 * - Implementor: FormRender (interface)
 * - ConcreteImplementor: HtmlRender, WidgetRender
 *
 * Le Bridge permet de varier indépendamment:
 * - Le type de formulaire (Login, Register, etc.)
 * - Le moteur de rendu (HTML, Widget/JSON)
 *
 * Exemple d'utilisation:
 * Form loginHtml = new LoginForm(new HtmlRender());
 * Form loginWidget = new LoginForm(new WidgetRender());
 * System.out.println(loginHtml.generate());  // HTML
 * System.out.println(loginWidget.generate()); // JSON
 */
public abstract class Form {

    // Reference vers l'implémentation (le "pont")
    protected FormRender renderEngine;
    protected Map<String, Map<String, String>> fields = new LinkedHashMap<>();

    /**
     * Constructeur avec injection du moteur de rendu
     * @param renderEngine Le moteur de rendu à utiliser
     */
    protected Form(FormRender renderEngine) {
        this.renderEngine = renderEngine;
    }

    /**
     * Ajoute un champ au formulaire
     * @param name Nom du champ
     * @param label Label affiché
     * @param type Type d'input (text, email, password, etc.)
     */
    protected void addField(String name, String label, String type) {
        Map<String, String> props = Map.of("label", label, "inputType", type);
        fields.put(name, props);
    }

    /**
     * Génère le formulaire en utilisant le moteur de rendu
     * @return Le formulaire rendu (HTML ou JSON selon le moteur)
     */
    public String generate() {
        return renderEngine.render(fields);
    }

    /**
     * Change le moteur de rendu dynamiquement
     * @param renderEngine Le nouveau moteur
     */
    public void setRenderEngine(FormRender renderEngine) {
        this.renderEngine = renderEngine;
    }
}
