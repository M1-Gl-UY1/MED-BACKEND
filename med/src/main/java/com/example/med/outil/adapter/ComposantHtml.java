package com.example.med.outil.adapter;

import com.example.med.model.commande_et_document.Commande;
import com.example.med.model.commande_et_document.LigneCommande;
import java.io.File;
import java.io.FileWriter;

/**
 * PATTERN ADAPTER - Adaptee (Composant adapté)
 *
 * Classe technique spécialisée dans la génération de documents HTML.
 * Cette classe est adaptée par DocumentHtml pour être utilisée
 * via l'interface DocumentInterface.
 */
public class ComposantHtml {

    /**
     * Génère un document HTML à partir des données de commande
     *
     * @param titre Le titre du document
     * @param commande Les données de la commande
     * @return L'URL relative du fichier HTML généré
     */
    public String htmlPrepareAffichage(String titre, Commande commande) {
        try {
            String folderPath = "documents";
            File folder = new File(folderPath);
            if (!folder.exists()) folder.mkdirs();

            String fileName = titre.replace(" ", "_") + "_" + System.currentTimeMillis() + ".html";
            String fullPath = folderPath + "/" + fileName;

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

            html.append("<h2>Détails de la commande</h2>");
            html.append("<table>");
            html.append("<tr><th>Véhicule</th><th>Quantité</th><th>Prix unitaire HT</th><th>Total HT</th></tr>");

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
}
