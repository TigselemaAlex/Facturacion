package com.capibaracode.backend.domain.entities;

import com.capibaracode.backend.util.enums.ClientType;
import com.capibaracode.backend.util.enums.IdentificationType;
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
    @Column(unique = true)
    private String identification;
    private String fullname;
    private String telephone;
    @Column(unique = true)
    private String email;
    private String address;
    private Boolean active;
    @Enumerated(EnumType.STRING)
    private ClientType type;
    @Enumerated(EnumType.STRING)
    private IdentificationType  identificationType;

    @PrePersist
    public void prePersist(){
        this.active = true;
    }

}
