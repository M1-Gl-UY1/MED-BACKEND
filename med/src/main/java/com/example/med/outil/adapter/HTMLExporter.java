package com.example.med.outil.adapter;

import com.example.med.model.commande_et_document.Document;

import java.io.File;
import java.io.FileWriter;

/**
 * PATTERN ADAPTER - Adapter (Adaptateur HTML)
 *
 * Exporte les documents au format HTML.
 * Conforme au schéma UML du projet.
 *
 * Note: Contrairement à DocumentPDFExporter, cette classe
 * n'utilise pas d'Adaptee externe car HTML est généré nativement.
 */
public class HTMLExporter implements DocumentExport {

    /**
     * Exporte le document au format HTML
     */
    @Override
    public void exporter(Document document) {
        try {
            // Construire le contenu HTML
            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html>\n");
            html.append("<html><head>");
            html.append("<meta charset='UTF-8'>");
            html.append("<title>").append(document.getType().name()).append("</title>");
            html.append("<style>");
            html.append("body { font-family: Arial, sans-serif; margin: 40px; }");
            html.append("h1 { color: #333; border-bottom: 2px solid #4CAF50; padding-bottom: 10px; }");
            html.append(".info { margin: 10px 0; }");
            html.append(".label { font-weight: bold; }");
            html.append("</style>");
            html.append("</head><body>");

            html.append("<h1>").append(document.getType().name()).append("</h1>");
            html.append("<div class='info'><span class='label'>Type:</span> ")
                    .append(document.getType()).append("</div>");
            html.append("<div class='info'><span class='label'>Format:</span> ")
                    .append(document.getFormat()).append("</div>");

            if (document.getLiasse() != null) {
                html.append("<div class='info'><span class='label'>Liasse ID:</span> ")
                        .append(document.getLiasse().getIdLiasse()).append("</div>");
            }

            html.append("</body></html>");

            // Générer le fichier
            String folderPath = "documents";
            File folder = new File(folderPath);
            if (!folder.exists()) folder.mkdirs();

            String fileName = document.getType().name() + "_" + System.currentTimeMillis() + ".html";
            String path = folderPath + "/" + fileName;

            FileWriter writer = new FileWriter(path);
            writer.write(html.toString());
            writer.close();

            // Mettre à jour l'URL du document
            document.setUrl("/documents/" + fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
