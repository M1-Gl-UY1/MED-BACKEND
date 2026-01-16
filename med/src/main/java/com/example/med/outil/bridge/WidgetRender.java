package com.example.med.outil.bridge;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * PATTERN BRIDGE - ConcreteImplementor
 *
 * Moteur de rendu Widget (JSON).
 * Génère des formulaires au format JSON pour les interfaces widget/mobile.
 */
public class WidgetRender implements FormRender {

    @Override
    public String render(Map<String, Map<String, String>> data) {
        String fieldsJson = data.entrySet().stream()
                .map(entry -> {
                    String name = entry.getKey();
                    String label = entry.getValue().getOrDefault("label", name);
                    String type = entry.getValue().getOrDefault("inputType", "text");

                    return String.format(
                            "    { \"widget\": \"input\", \"name\": \"%s\", \"label\": \"%s\", \"type\": \"%s\" }",
                            name, label, type
                    );
                })
                .collect(Collectors.joining(",\n"));

        return """
        {
          "type": "form",
          "method": "post",
          "fields": [
        %s
          ],
          "submit": { "label": "Envoyer" }
        }
        """.formatted(fieldsJson);
    }
}
