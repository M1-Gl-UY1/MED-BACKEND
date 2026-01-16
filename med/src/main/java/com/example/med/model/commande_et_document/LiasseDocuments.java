package com.example.med.model.commande_et_document;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * PATTERN SINGLETON - Singleton
 *
 * Cette classe implémente le pattern Singleton pour la "liasse vierge".
 * Une seule instance de liasse vierge existe dans le système.
 *
 * Structure du pattern:
 * - Attribut static privé: instance
 * - Constructeur protected: empêche l'instanciation directe
 * - Méthode static getInstance(): retourne l'instance unique
 *
 * Note: creerNouvelleInstance() permet au Builder de créer de nouvelles liasses
 * (nécessaire pour le pattern Builder) tout en gardant le Singleton pour la liasse vierge.
 *
 * Exemple d'utilisation:
 * LiasseDocuments liasseVierge = LiasseDocuments.getInstance();
 */
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

    // ============================================
    // PATTERN SINGLETON - Instance unique
    // ============================================

    @Transient
    @JsonIgnore
    private static LiasseDocuments instance = null;

    /**
     * Constructeur PROTECTED - Empêche l'instanciation directe
     * Permet à Hibernate et aux méthodes statiques de fonctionner
     */
    protected LiasseDocuments() {
        super();
    }

    /**
     * Retourne l'instance unique de la liasse vierge (Thread-safe)
     * @return L'instance unique du Singleton
     */
    public static synchronized LiasseDocuments getInstance() {
        if (instance == null) {
            instance = new LiasseDocuments();
        }
        return instance;
    }

    /**
     * Méthode de fabrication pour le Builder
     * Permet de créer de nouvelles liasses (nécessaire pour Builder)
     * @return Une nouvelle instance de LiasseDocuments
     */
    public static LiasseDocuments creerNouvelleInstance() {
        return new LiasseDocuments();
    }

    // ============================================
    // MÉTHODES MÉTIER
    // ============================================

    /**
     * Ajoute un document à la liasse
     * @param document Le document à ajouter
     * @return Le document ajouté
     */
    public Document ajouterDocument(Document document) {
        if (this.documents == null) {
            this.documents = new ArrayList<>();
        }
        this.documents.add(document);
        document.setLiasse(this);
        return document;
    }

    /**
     * Retourne le nombre de documents dans la liasse
     * @return Le nombre de documents
     */
    public int getNombreDocuments() {
        return documents != null ? documents.size() : 0;
    }
}
