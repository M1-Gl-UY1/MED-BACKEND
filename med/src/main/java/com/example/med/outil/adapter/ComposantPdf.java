package com.example.med.outil.adapter;

class ComposantPdf {
    public void pdfFixeContenu(String contenu) {
        System.out.println("Contenu PDF fixé : " + contenu);
    }
    public void pdfPrepareAffichage() { System.out.println("Affichage PDF..."); }
    public void pdfRafraichit() { System.out.println("Rafraîchissement PDF..."); }
    public void pdfTermineAffichage() { System.out.println("Fin PDF."); }
}