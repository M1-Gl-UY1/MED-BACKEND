package com.example.med.outil.adapter;

import com.example.med.model.commande_et_document.Commande;
import com.example.med.model.commande_et_document.LigneCommande;
import com.example.med.model.catalogue.Option;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.UnitValue;
import java.io.File;

public class ComposantPdf {

    public String pdfPrepareAffichage(String titreDoc, Commande commande) {
        try {
            // 1. Gestion du dossier local
            String folderPath = "documents";
            File folder = new File(folderPath);
            if (!folder.exists()) folder.mkdirs();

            String fileName = titreDoc.replace(" ", "_") + "_" + System.currentTimeMillis() + ".pdf";
            String fullPath = folderPath + "/" + fileName;

            PdfWriter writer = new PdfWriter(fullPath);
            PdfDocument pdf = new PdfDocument(writer);
            com.itextpdf.layout.Document layoutDoc = new com.itextpdf.layout.Document(pdf);

            // 2. Entête du document
            layoutDoc.add(new Paragraph(titreDoc.toUpperCase()).setBold().setFontSize(18));
            layoutDoc.add(new Paragraph("Date : " + commande.getDate()));
            layoutDoc.add(new Paragraph("------------------------------------------------------------------"));

            // 3. Section Client
            if (commande.getUtilisateur() != null) {
                layoutDoc.add(new Paragraph("INFORMATIONS CLIENT").setBold());
                layoutDoc.add(new Paragraph("Nom : " + commande.getUtilisateur().getNom()));
                layoutDoc.add(new Paragraph("Pays de livraison : " + commande.getPaysLivraison()));
            }

            layoutDoc.add(new Paragraph("\nDÉTAILS DU VÉHICULE / COMMANDE").setBold());

            // 4. Tableau des produits (Véhicules et Options)
            // Colonnes : Description, Quantité, Prix Unit HT, Total HT
            float[] columnWidths = {4, 1, 2, 2};
            Table table = new Table(UnitValue.createPointArray(columnWidths));
            table.setWidth(UnitValue.createPercentValue(100));

            table.addHeaderCell(new Cell().add(new Paragraph("Description")));
            table.addHeaderCell(new Cell().add(new Paragraph("Qté")));
            table.addHeaderCell(new Cell().add(new Paragraph("Prix Unit HT")));
            table.addHeaderCell(new Cell().add(new Paragraph("Total HT")));

            for (LigneCommande ligne : commande.getLignesCommandes()) {
                // Ligne du véhicule
                String nomVehicule = (ligne.getVehicule() != null) ? ligne.getVehicule().getNom() : "Véhicule inconnu";
                table.addCell(new Cell().add(new Paragraph(nomVehicule).setBold()));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(ligne.getQuantite()))));
                table.addCell(new Cell().add(new Paragraph(ligne.getPrixUnitaireHT() + " €")));
                table.addCell(new Cell().add(new Paragraph((ligne.getPrixUnitaireHT() * ligne.getQuantite()) + " €")));

                
            }
            layoutDoc.add(table);

            // 5. Section Totaux
            layoutDoc.add(new Paragraph("\n"));
            layoutDoc.add(new Paragraph("Total HT : " + (commande.getTotal() - commande.getTaxe()) + " €"));
            layoutDoc.add(new Paragraph("Taxes : " + commande.getTaxe() + " €"));
            layoutDoc.add(new Paragraph("TOTAL À PAYER (TTC) : " + commande.getTotal() + " €").setBold().setFontSize(14));

            layoutDoc.close();
            return "/documents/" + fileName;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}