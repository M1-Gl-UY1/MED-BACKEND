package com.example.med.outil.bridge;

import java.util.Map;

public class HtmlRender implements FormRender {
    @Override
    public String render(Map<String, Map<String, String>> data) {
        StringBuilder sb = new StringBuilder("<form>\n");
        data.forEach((name, props) -> {
            sb.append("  <label>").append(props.get("label")).append("</label>\n");
            sb.append("  <input type='").append(props.get("inputType")).append("' name='").append(name).append("'>\n");
        });
        sb.append("</form>");
        return sb.toString();
    }
}