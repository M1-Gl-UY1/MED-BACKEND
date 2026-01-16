package com.example.med.outil.adapter;

import com.example.med.model.commande_et_document.Commande;
import com.example.med.model.commande_et_document.Document;
import com.example.med.model.commande_et_document.LigneCommande;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;

import java.io.File;

/**
 * PATTERN ADAPTER - Adaptee (Composant technique unifie)
 *
 * Classe technique specialisee dans la generation de documents PDF.
 * Fusionne les anciennes classes ComposantPdf et PDFLIBRARY.
 *
 * Utilisee par:
 * - DocumentPdf (pour exporter des Commandes)
 * - DocumentPDFExporter (pour exporter des Documents de liasse)
 */
public class PdfGenerator {

    private static final String FOLDER_PATH = "documents";

    /**
     * Genere un document PDF a partir des donnees de commande
     *
     * @param titreDoc Le titre du document
     * @param commande Les donnees de la commande
     * @return L'URL relative du fichier PDF genere
     */
    public String genererPdfCommande(String titreDoc, Commande commande) {
        try {
            creerDossierSiNecessaire();

            String fileName = titreDoc.replace(" ", "_") + "_" + System.currentTimeMillis() + ".pdf";
            String fullPath = FOLDER_PATH + "/" + fileName;

            PdfWriter writer = new PdfWriter(fullPath);
            PdfDocument pdf = new PdfDocument(writer);
            com.itextpdf.layout.Document layoutDoc = new com.itextpdf.layout.Document(pdf);

            // Entete du document
            layoutDoc.add(new Paragraph(titreDoc.toUpperCase()).setBold().setFontSize(18));
            layoutDoc.add(new Paragraph("Date : " + commande.getDate()));
            layoutDoc.add(new Paragraph("------------------------------------------------------------------"));

            // Section Client
            if (commande.getUtilisateur() != null) {
                layoutDoc.add(new Paragraph("INFORMATIONS CLIENT").setBold());
                layoutDoc.add(new Paragraph("Nom : " + commande.getUtilisateur().getNom()));
                layoutDoc.add(new Paragraph("Pays de livraison : " + commande.getPaysLivraison()));
            }

            layoutDoc.add(new Paragraph("\nDETAILS DU VEHICULE / COMMANDE").setBold());

            // Tableau des produits
            float[] columnWidths = {4, 1, 2, 2};
            Table table = new Table(UnitValue.createPointArray(columnWidths));
            table.setWidth(UnitValue.createPercentValue(100));

            table.addHeaderCell(new Cell().add(new Paragraph("Description")));
            table.addHeaderCell(new Cell().add(new Paragraph("Qte")));
            table.addHeaderCell(new Cell().add(new Paragraph("Prix Unit HT")));
            table.addHeaderCell(new Cell().add(new Paragraph("Total HT")));

            for (LigneCommande ligne : commande.getLignesCommandes()) {
                String nomVehicule = (ligne.getVehicule() != null) ? ligne.getVehicule().getNom() : "Vehicule inconnu";
                table.addCell(new Cell().add(new Paragraph(nomVehicule).setBold()));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(ligne.getQuantite()))));
                table.addCell(new Cell().add(new Paragraph(ligne.getPrixUnitaireHT() + " €")));
                table.addCell(new Cell().add(new Paragraph((ligne.getPrixUnitaireHT() * ligne.getQuantite()) + " €")));
            }
            layoutDoc.add(table);

            // Section Totaux
            layoutDoc.add(new Paragraph("\n"));
            layoutDoc.add(new Paragraph("Total HT : " + (commande.getTotal() - commande.getTaxe()) + " €"));
            layoutDoc.add(new Paragraph("Taxes : " + commande.getTaxe() + " €"));
            layoutDoc.add(new Paragraph("TOTAL A PAYER (TTC) : " + commande.getTotal() + " €").setBold().setFontSize(14));

            layoutDoc.close();
            return "/documents/" + fileName;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Genere un document PDF a partir d'un Document de liasse
     *
     * @param document Le document a exporter
     * @return L'URL relative du fichier PDF genere
     */
    public String genererPdfDocument(Document document) {
        try {
            creerDossierSiNecessaire();

            String fileName = document.getType().name() + "_" + System.currentTimeMillis() + ".pdf";
            String fullPath = FOLDER_PATH + "/" + fileName;

            PdfWriter writer = new PdfWriter(fullPath);
            PdfDocument pdf = new PdfDocument(writer);
            com.itextpdf.layout.Document layoutDoc = new com.itextpdf.layout.Document(pdf);

            // Titre
            layoutDoc.add(new Paragraph(document.getType().name()).setBold().setFontSize(18));

            // Informations du document
            layoutDoc.add(new Paragraph("Type de document: " + document.getType()));
            layoutDoc.add(new Paragraph("Format: " + document.getFormat()));

            if (document.getLiasse() != null) {
                layoutDoc.add(new Paragraph("Liasse ID: " + document.getLiasse().getIdLiasse()));
            }

            layoutDoc.close();
            return "/documents/" + fileName;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void creerDossierSiNecessaire() {
        File folder = new File(FOLDER_PATH);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }
}
