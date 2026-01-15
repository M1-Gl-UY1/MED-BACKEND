package com.example.med.outil.adapter;

import java.io.File;


import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;


public class ComposantPdf {
    private String contenu;

    public void pdfFixeContenu(String contenu) {
        this.contenu = contenu;
    }

    public String pdfPrepareAffichage(String nomFichier) {
        try {
            String chemin = "documents/" + nomFichier + ".pdf";
            File directory = new File("documents/");
            if (!directory.exists()) directory.mkdirs();

            PdfWriter writer = new PdfWriter(chemin);
            PdfDocument pdf = new PdfDocument(writer);
            
            
            com.itextpdf.layout.Document layoutDoc = new com.itextpdf.layout.Document(pdf);
            
            layoutDoc.add(new Paragraph("DOCUMENT OFFICIEL VÉHICULE"));
            layoutDoc.add(new Paragraph("----------------------------"));
            layoutDoc.add(new Paragraph(this.contenu != null ? this.contenu : "Pas de contenu"));
            
            layoutDoc.close(); 
            return chemin;
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de la génération : " + e.getMessage();
        }
    }
}