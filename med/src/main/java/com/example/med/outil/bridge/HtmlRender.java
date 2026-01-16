package com.example.med.outil.bridge;

import java.util.Map;

/**
 * PATTERN BRIDGE - ConcreteImplementor
 *
 * Moteur de rendu HTML.
 * Génère des formulaires au format HTML standard.
 */
public class HtmlRender implements FormRender {

    @Override
    public String render(Map<String, Map<String, String>> data) {
        StringBuilder sb = new StringBuilder();
        sb.append("<form method='post'>\n");

        data.forEach((name, props) -> {
            sb.append("  <div class='form-group'>\n");
            sb.append("    <label for='").append(name).append("'>")
                    .append(props.get("label")).append("</label>\n");
            sb.append("    <input type='").append(props.get("inputType"))
                    .append("' name='").append(name)
                    .append("' id='").append(name).append("'>\n");
            sb.append("  </div>\n");
        });

        sb.append("  <button type='submit'>Envoyer</button>\n");
        sb.append("</form>");
        return sb.toString();
    }
}
