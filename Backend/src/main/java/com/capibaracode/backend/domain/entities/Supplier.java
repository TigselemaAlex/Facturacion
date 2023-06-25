package com.capibaracode.backend.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 13, unique = true)
    private String identification;

    @Column(nullable = false, length = 80, unique = true)
    private String name;

    @Column(nullable = false, length = 120)
    private String email;

    @Column(nullable = false, length = 150)
    private String address;

    @Column(nullable = false, length = 20)
    private String telephone;

    @Column(nullable = false)
    @ColumnDefault("true")
    private Boolean status;

}
