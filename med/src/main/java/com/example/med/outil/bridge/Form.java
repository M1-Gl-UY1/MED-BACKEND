package com.example.med.outil.bridge;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Form {
    protected FormRender renderEngine;
    protected Map<String, Map<String, String>> fields = new LinkedHashMap<>();

    protected Form(FormRender renderEngine) {
        this.renderEngine = renderEngine;
    }

    protected void addField(String name, String label, String type) {
        Map<String, String> props = Map.of("label", label, "inputType", type);
        fields.put(name, props);
    }

    public String generate() {
        return renderEngine.render(fields);
    }
}

class test{
    static void main() {
        Form login = new LoginForm(new HtmlRender());
        System.out.println(login.generate());

        login = new LoginForm(new WidgetRender());
        System.out.println(login.generate());
    }
}