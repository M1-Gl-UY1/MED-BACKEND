package com.example.med.model.catalogue;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_stock")
    private long idStock;

    private int quantite;

    @Column(name = "date_entre")
    private LocalDate dateEntre;
}
