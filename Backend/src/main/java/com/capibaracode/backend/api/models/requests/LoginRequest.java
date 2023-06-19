package com.capibaracode.backend.api.models.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotNull(message = "El correo es obligatorio")
        @Email(message = "Debe de tener un formato válido")
        String email,
        @NotNull(message = "La contraseña es obligatoria")
        @Size( min = 8, message = "La contraseña debe tener minimo 8 caracteres")
        String password) {
}
