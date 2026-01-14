package com.example.med.repository;

import com.example.med.model.panier.LignePanier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LignePanierRepository extends JpaRepository<LignePanier,Long> {
}
