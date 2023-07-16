package com.capibaracode.backend.api.models.requests;

import com.capibaracode.backend.util.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@NoArgsConstructor
public class UserRequest {


    private String password;

    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(max = 80, message = "El nombre completo no puede tener mas de 80 caracteres")
    private String fullName;


    @NotBlank(message = "La identificación es obligatoria")
    @Size(max = 20, message = "El número de identificación no puede superar los 20 caracteres")
    private String identification;

    private Boolean status;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene el formato correcto")
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    private String telephone;

    @NotBlank(message = "El rol es obligatorio")
    private Role role;

}
