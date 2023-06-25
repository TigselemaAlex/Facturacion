package com.capibaracode.backend.api.models.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class SupplierRequest {

    @NotBlank(message = "La identificación es obligatorio.")
    @Size(min = 10,max = 13, message = "La identificacion debe tener de 10 a 13 caracteres.")
    private String identification;

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres.")
    private String name;

    @NotBlank(message = "El email es obligatorio.")
    @Size(max = 120, message = "El email no puede tener más de 120 caracteres.")
    @Email(message = "El email no tiene el formato correcto.")
    private String email;

    @NotBlank(message = "La direccion es obligatorio.")
    @Size(max = 150, message = "La dirección no puede tener más de 150 caracteres.")
    private String address;

    @NotBlank(message = "El teléfono es obligatorio.")
    @Size(max = 20, message = "El teléfono no puede tener más de 20 caracteres.")
    private String telephone;

    @NotNull(message = "El estado es obligatorio")
    private Boolean status;

}
