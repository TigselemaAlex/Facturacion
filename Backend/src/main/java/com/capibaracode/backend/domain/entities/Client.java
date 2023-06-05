package com.capibaracode.backend.domain.entities;

import com.capibaracode.backend.util.enums.ClientType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String identification;
    private String fullname;
    private String telephone;
    private String email;
    @Enumerated(EnumType.STRING)
    private ClientType type;

}
