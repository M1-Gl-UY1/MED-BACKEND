package com.example.med.outil.adapter;

public class DocumentPdf implements DocumentInterface {
    private ComposantPdf outilPdf = new ComposantPdf();

    @Override
    public void setContenu(String contenu) {
        outilPdf.pdfFixeContenu(contenu);
    }

    @Override
    public void imprimer() {
        outilPdf.pdfPrepareAffichage();
        // Logique d'impression
        System.out.println("Impression du document PDF en cours.");
    }
}
