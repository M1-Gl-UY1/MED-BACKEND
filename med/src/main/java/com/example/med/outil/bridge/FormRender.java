package com.example.med.outil.bridge;

import java.util.Map;

public interface FormRender {
    String render(Map<String, Map<String, String>> data);
}