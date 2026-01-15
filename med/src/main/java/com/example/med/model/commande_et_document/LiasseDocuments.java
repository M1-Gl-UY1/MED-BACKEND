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

    // --- LOGIQUE SINGLETON STRICTE ---
    
    @Transient 
    @JsonIgnore 
    private static LiasseDocuments instance = null;

    // 1. Constructeur PROTECTED : Empêche l'instanciation directe (new) depuis l'extérieur
    // mais permet à Hibernate et aux méthodes statiques de fonctionner.
    protected LiasseDocuments() {
        super();
    }

    // 2. Méthode getInstance (Thread-safe) : C'est l'instance unique pour la "Liasse Vierge"
    public static synchronized LiasseDocuments getInstance() {
        if (instance == null) {
            instance = new LiasseDocuments();
        }
        return instance;
    }

    // 3. Méthode de fabrication pour le Builder : 
    // Puisque le constructeur est protégé, on offre cette méthode pour créer de nouvelles liasses
    public static LiasseDocuments creerNouvelleInstance() {
        return new LiasseDocuments();
    }

    // --- MÉTHODES MÉTIER ---

    public Document ajouterDocument(Document document) {
        if (this.documents == null) this.documents = new ArrayList<>();
        this.documents.add(document);
        document.setLiasse(this); 
        return document;
    }
}