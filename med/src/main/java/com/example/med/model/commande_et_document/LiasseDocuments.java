package com.example.med.model.commande_et_document;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class LiasseDocuments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLiasse;

    @OneToMany(mappedBy = "liasse", cascade = CascadeType.ALL)
    private List<Document> documents = new ArrayList<>();

    private static LiasseDocuments instance = null;

    
    protected LiasseDocuments() {}

    public static LiasseDocuments getInstance() {
        if (instance == null) {
            instance = new LiasseDocuments();
        }
        return instance;
    }

    public void ajouterDocument(Document doc) {
        this.documents.add(doc);
        doc.setLiasse(this);
    }
}
