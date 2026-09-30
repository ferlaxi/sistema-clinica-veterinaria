package com.clinica_veterinaria.veterinaria.dto;

import com.clinica_veterinaria.veterinaria.entity.enums.Rol;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDto {
    private Long id;
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private Rol rol;
    private boolean activo;
}
