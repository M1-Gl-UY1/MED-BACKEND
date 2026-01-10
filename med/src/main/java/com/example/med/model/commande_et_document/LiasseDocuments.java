package com.example.med.model.commande_et_document;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class LiasseDocuments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_liasse_de_documents")
    private Long idLiasse;
}
