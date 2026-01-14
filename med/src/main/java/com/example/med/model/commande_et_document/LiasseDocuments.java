package com.example.med.model.commande_et_document;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class LiasseDocuments {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_liasse_de_documents")
    private Long idLiasse;

    
    @OneToMany(mappedBy = "liasse", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference 
    private List<Document> documents = new ArrayList<>();

    
    
    @Transient 
    @JsonIgnore 
    private static LiasseDocuments instance = null;

    
    public LiasseDocuments() {
        super();
    }

    
    public static LiasseDocuments getInstance() {
        if (instance == null) {
            instance = new LiasseDocuments();
        }
        return instance;
    }

    
    public Document ajouterDocument(Document document) {
        this.documents.add(document);
        document.setLiasse(this); 
        return document;
    }
}