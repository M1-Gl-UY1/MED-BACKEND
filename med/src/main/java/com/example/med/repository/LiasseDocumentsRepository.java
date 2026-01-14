package com.example.med.repository;

import com.example.med.model.commande_et_document.LiasseDocuments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LiasseDocumentsRepository extends JpaRepository<LiasseDocuments, Long> {
}
