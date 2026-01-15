package com.example.med.outil.adapter;

import com.example.med.model.commande_et_document.Commande;
import com.example.med.model.commande_et_document.LigneCommande;
import java.io.File;
import java.io.FileWriter;

public class ComposantHtml {
    public String htmlPrepareAffichage(String titre, Commande commande) {
        try {
            String folderPath = "documents";
            File folder = new File(folderPath);
            if (!folder.exists()) folder.mkdirs();

            String fileName = titre.replace(" ", "_") + "_" + System.currentTimeMillis() + ".html";
            String fullPath = folderPath + "/" + fileName;

            StringBuilder html = new StringBuilder();
            html.append("<html><head><style>table {width:100%; border-collapse:collapse;} th,td {border:1px solid black; padding:8px;}</style></head><body>");
            html.append("<h1>").append(titre).append("</h1>");
            html.append("<p>Client : ").append(commande.getUtilisateur().getNom()).append("</p>");
            
            html.append("<table><tr><th>Véhicule</th><th>Quantité</th><th>Prix HT</th></tr>");
            for (LigneCommande ligne : commande.getLignesCommandes()) {
                html.append("<tr>")
                    .append("<td>").append(ligne.getVehicule().getNom()).append("</td>")
                    .append("<td>").append(ligne.getQuantite()).append("</td>")
                    .append("<td>").append(ligne.getPrixUnitaireHT()).append(" €</td>")
                    .append("</tr>");
            }
            html.append("</table>");
            html.append("<h3>Total TTC : ").append(commande.getTotal()).append(" €</h3>");
            html.append("</body></html>");

            FileWriter writer = new FileWriter(fullPath);
            writer.write(html.toString());
            writer.close();

            return "/documents/" + fileName;
        } catch (Exception e) {
            return null;
        }
    }
}