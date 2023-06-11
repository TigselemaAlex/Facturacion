package com.capibaracode.backend.api.models.requests;

import com.capibaracode.backend.util.enums.CompanyType;
import com.capibaracode.backend.util.enums.EnvironmentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RegisterRequest {


    @NotBlank(message = "El nombre de la compania/negocio es obligatorio")
    @Size(max = 80, message = "El nombre de la compania/negocio no puede tener más de 80 caracteres")
    private String name;

    @NotBlank(message = "El email de la compania/negocio es obligatorio")
    @Size(max = 120, message = "El email de la compania/negocio no puede tener más de 120 caracteres")
    private String email;

    @NotBlank(message = "El ruc de la compania/negocio es obligatorio")
    @Size(max = 20, message = "El ruc de la compania/negocio no puede tener más de 20 caracteres")
    private String ruc;

    @NotBlank(message = "El teléfono de la compania/negocio es obligatorio")
    @Size(max = 20, message = "El teléfono de la compania/negocio no puede tener más de 20 caracteres")
    private String phone;

    @NotBlank(message = "La dirección de la compania/negocio es obligatorio")
    @Size(max = 150, message = "La dirección de la compania/negocio no puede tener más de 150 caracteres")
    private String address;
    
    @NotNull(message = "La opcion de llevar contabilidad es obligatoria")
    private Boolean accounting;

    @NotBlank(message = "El tipo de la compania/negocio es obligatorio")
    private CompanyType type;

    @NotBlank(message = "El tipo de ambiente es obligatorio")
    private EnvironmentType environment;

    private String logo;

}
