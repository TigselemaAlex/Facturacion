package com.capibaracode.backend.api.models.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRequest {
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(max = 20, message = "El nombre de usuario no puede tener mas de 20 caracteres")
    private String username;

    //@Column(nullable = false, length = 16)
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 16, message = "La contraseña debe tener de 8 a 16 caracteres")
    private String password;

    //Column(nullable = false, length = 80)
    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(max = 80, message = "El nombre completo no puede tener mas de 80 caracteres")
    private String fullName;

    //@Column(nullable = false, unique = true, length = 20)
    @NotBlank(message = "La identificación es obligatoria")
    @Size(max = 20, message = "El número de identificación no puede superar los 20 caracteres")
    private String identification;

}
