package com.capibaracode.backend.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(nullable = false)
    private Boolean status;

    @OneToOne
    private Promotion promotion;
    @OneToOne
    private Tax tax;
}
