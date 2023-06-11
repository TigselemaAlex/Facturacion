package com.capibaracode.backend.domain.entities;

import com.capibaracode.backend.util.enums.CompanyType;
import com.capibaracode.backend.util.enums.EnvironmentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "auth_schema")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Column(nullable = false, length = 80, unique = true)
    private String name;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(nullable = false, unique = true, length = 20)
    private String ruc;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false, length = 150)
    private String address;

    private String logo;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean accounting;

    @Enumerated(EnumType.STRING)
    private CompanyType type;

    @Enumerated(EnumType.STRING)
    private EnvironmentType environment;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "company")
    private Set<User> users = new HashSet<>();

    public void addUser(@NotNull User user){
        users.add(user);
    }
}
