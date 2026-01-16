package com.example.med.outil.adapter;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;

/**
 * PATTERN ADAPTER - Adaptee (Composant externe)
 *
 * Bibliothèque externe de génération PDF.
 * Cette classe représente une API tierce que nous devons adapter.
 * Conforme au schéma UML du projet.
 */
public class PDFLIBRARY {

    /**
     * Génère un fichier PDF avec le contenu spécifié
     * @param content Le contenu textuel du PDF
     * @param path Le chemin où sauvegarder le fichier
     */
    public void generatePDF(String content, String path) {
        try {
            // Créer le dossier si nécessaire
            File file = new File(path);
            file.getParentFile().mkdirs();

            PdfWriter writer = new PdfWriter(path);
            PdfDocument pdf = new PdfDocument(writer);
            com.itextpdf.layout.Document layoutDoc = new com.itextpdf.layout.Document(pdf);

            // Ajouter le contenu
            String[] lines = content.split("\n");
            for (String line : lines) {
                if (line.startsWith("# ")) {
                    layoutDoc.add(new Paragraph(line.substring(2)).setBold().setFontSize(18));
                } else if (line.startsWith("## ")) {
                    layoutDoc.add(new Paragraph(line.substring(3)).setBold().setFontSize(14));
                } else {
                    layoutDoc.add(new Paragraph(line));
                }
            }

            layoutDoc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
