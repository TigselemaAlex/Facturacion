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

    @Column(nullable = false, length = 10)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Double quantity;

    @Column(nullable = false)
    private Boolean status;

    @Column(nullable = false)
    private Integer minStock;

    @Column(nullable = false)
    private Integer maxStock;

    @ManyToOne
    private Category category;
    @ManyToOne
    private Promotion promotion;
    @ManyToOne
    private Tax tax;
}
