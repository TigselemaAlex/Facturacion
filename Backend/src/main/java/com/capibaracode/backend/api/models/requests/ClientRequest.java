package com.capibaracode.backend.api.models.requests;

import com.capibaracode.backend.util.enums.IdentificationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClientRequest {
    @NotNull(message = "El tipo de identificación es obligatorio")
    private String identification;
    @NotNull(message = "El nombre completo es obligatorio")
    private String fullname;
    @NotNull(message = "El número de teléfono es obligatorio")
    private String telephone;
    @NotNull(message = "El correo electrónico es obligatorio")
    @Email(message = "Debe de tener un formato válido")
    private String email;
    @NotNull(message = "La dirección es obligatoria")
    private String address;
    private Boolean status;
    @NotNull(message = "El tipo de identificación es obligatorio")
    private IdentificationType identificationType;
}
