package com.example.med.repository;

import com.example.med.model.catalogue.ImageVehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ImageVehiculeRepository extends JpaRepository<ImageVehicule, Long> {

    List<ImageVehicule> findByVehiculeIdVehiculeOrderByOrdreAffichageAsc(Long idVehicule);

    ImageVehicule findByVehiculeIdVehiculeAndEstPrincipaleTrue(Long idVehicule);
}
