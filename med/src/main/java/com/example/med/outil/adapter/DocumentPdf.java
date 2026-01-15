package com.example.med.outil.adapter;

public class DocumentPdf implements DocumentInterface {
    private ComposantPdf outilPdf = new ComposantPdf();
    private String nomFichier;

    
    public DocumentPdf(String nomFichier) {
        this.nomFichier = nomFichier;
    }

    @Override
    public void setContenu(String contenu) {
        outilPdf.pdfFixeContenu(contenu);
    }

    
    @Override
    public String imprimer() {
        
        return outilPdf.pdfPrepareAffichage(this.nomFichier);
    }
}