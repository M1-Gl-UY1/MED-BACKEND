package com.example.med.outil.adapter;

import com.example.med.model.commande_et_document.Commande;
import com.example.med.model.commande_et_document.Document;
import com.example.med.model.commande_et_document.LigneCommande;

import java.io.File;
import java.io.FileWriter;

/**
 * PATTERN ADAPTER - Adaptee (Composant technique unifie)
 *
 * Classe technique specialisee dans la generation de documents HTML.
 * Fusionne les anciennes classes ComposantHtml et la logique de HTMLExporter.
 *
 * Utilisee par:
 * - DocumentHtml (pour exporter des Commandes)
 * - HTMLExporter (pour exporter des Documents de liasse)
 */
public class HtmlGenerator {

    private static final String FOLDER_PATH = "documents";

    /**
     * Genere un document HTML a partir des donnees de commande
     *
     * @param titre Le titre du document
     * @param commande Les donnees de la commande
     * @return L'URL relative du fichier HTML genere
     */
    public String genererHtmlCommande(String titre, Commande commande) {
        try {
            creerDossierSiNecessaire();

            String fileName = titre.replace(" ", "_") + "_" + System.currentTimeMillis() + ".html";
            String fullPath = FOLDER_PATH + "/" + fileName;

            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html>\n");
            html.append("<html><head>");
            html.append("<meta charset='UTF-8'>");
            html.append("<style>");
            html.append("body { font-family: Arial, sans-serif; margin: 20px; }");
            html.append("table { width: 100%; border-collapse: collapse; margin: 20px 0; }");
            html.append("th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }");
            html.append("th { background-color: #4CAF50; color: white; }");
            html.append("h1 { color: #333; }");
            html.append(".total { font-size: 1.2em; font-weight: bold; }");
            html.append("</style>");
            html.append("</head><body>");

            html.append("<h1>").append(titre).append("</h1>");
            html.append("<p><strong>Date:</strong> ").append(commande.getDate()).append("</p>");
            html.append("<p><strong>Client:</strong> ").append(commande.getUtilisateur().getNom()).append("</p>");
            html.append("<p><strong>Pays de livraison:</strong> ").append(commande.getPaysLivraison()).append("</p>");

            html.append("<h2>Details de la commande</h2>");
            html.append("<table>");
            html.append("<tr><th>Vehicule</th><th>Quantite</th><th>Prix unitaire HT</th><th>Total HT</th></tr>");

            for (LigneCommande ligne : commande.getLignesCommandes()) {
                double totalLigne = ligne.getPrixUnitaireHT() * ligne.getQuantite();
                html.append("<tr>")
                        .append("<td>").append(ligne.getVehicule().getNom()).append("</td>")
                        .append("<td>").append(ligne.getQuantite()).append("</td>")
                        .append("<td>").append(ligne.getPrixUnitaireHT()).append(" €</td>")
                        .append("<td>").append(totalLigne).append(" €</td>")
                        .append("</tr>");
            }
            html.append("</table>");

            html.append("<div class='total'>");
            html.append("<p>Total HT: ").append(commande.getTotal() - commande.getTaxe()).append(" €</p>");
            html.append("<p>Taxes: ").append(commande.getTaxe()).append(" €</p>");
            html.append("<p>Total TTC: ").append(commande.getTotal()).append(" €</p>");
            html.append("</div>");

            html.append("</body></html>");

            FileWriter writer = new FileWriter(fullPath);
            writer.write(html.toString());
            writer.close();

            return "/documents/" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Genere un document HTML a partir d'un Document de liasse
     *
     * @param document Le document a exporter
     * @return L'URL relative du fichier HTML genere
     */
    public String genererHtmlDocument(Document document) {
        try {
            creerDossierSiNecessaire();

            String fileName = document.getType().name() + "_" + System.currentTimeMillis() + ".html";
            String fullPath = FOLDER_PATH + "/" + fileName;

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

            FileWriter writer = new FileWriter(fullPath);
            writer.write(html.toString());
            writer.close();

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
