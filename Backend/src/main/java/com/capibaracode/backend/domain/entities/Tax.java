package com.capibaracode.backend.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private String tax;
    private Double percentage;
    private Boolean status;
}
