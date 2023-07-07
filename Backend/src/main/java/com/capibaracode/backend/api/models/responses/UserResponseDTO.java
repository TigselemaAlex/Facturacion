package com.capibaracode.backend.api.models.responses;

import com.capibaracode.backend.util.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private UUID id;

    private Boolean status;

    private String fullName;

    private String email;

    private String identification;

    private String telephone;

    private Role role;

}
