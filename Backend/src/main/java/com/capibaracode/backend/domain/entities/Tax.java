package com.capibaracode.backend.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class Tax {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 50)
    private String tax;

    @Column(nullable = false)
    @Min(value = 0, message = "El número debe ser mayor o igual a cero")
    @Max(value = 99, message = "El número debe tener un máximo de 2 dígitos")
    private Double percentage;

    @Column(nullable = false)
    private Boolean status;
}
