package com.capibaracode.backend.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String code;
    private String name;
    private Double price;
    private Double quantity;
    private Boolean status;
    private Integer minStocK;
    private Integer maxStock;

    @ManyToOne
    private Category category;
    @ManyToOne
    private Promotion promotion;
    @ManyToOne
    private Tax tax;
}
