package com.example.med.model.commande_et_document;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_document")
    private Long idDocument;

    @Enumerated(EnumType.STRING)
    private TypeDocument type;

    @Enumerated(EnumType.STRING)
    private TypeFormat format;

    private String url;

    @ManyToOne
    @JoinColumn(name = "id_liasse_de_documents")
    private LiasseDocuments liasse;
}
