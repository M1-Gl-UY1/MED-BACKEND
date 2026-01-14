package com.example.med.outil.bridge;
import java.util.Map;
import java.util.stream.Collectors;

public class WidgetRender implements FormRender {

    @Override
    public String render(Map<String, Map<String, String>> data) {
        // On transforme chaque entrée de la Map en une ligne d'objet JSON
        String fieldsJson = data.entrySet().stream()
                .map(entry -> {
                    String name = entry.getKey();
                    String label = entry.getValue().getOrDefault("label", name);
                    String type = entry.getValue().getOrDefault("inputType", "text");

                    // Retourne un objet JSON pour chaque champ
                    return String.format(
                            "    { \"widget\": \"input\", \"name\": \"%s\", \"label\": \"%s\", \"type\": \"%s\" }",
                            name, label, type
                    );
                })
                .collect(Collectors.joining(",\n"));

        // Structure globale du JSON
        return """
        {
          "type": "form",
          "fields": [
        %s
          ]
        }
        """.formatted(fieldsJson);
    }
}