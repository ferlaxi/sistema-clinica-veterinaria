package com.clinica_veterinaria.veterinaria.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.List;
import java.util.ArrayList;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DuenioDto {
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    private String apellido;

    @NotBlank(message = "El DNI no puede estar vacío")
    private String dni;

    @NotBlank(message = "El teléfono no puede estar vacío")
    private String telefono;

    private String direccion;
    private Long usuarioId;

    @Builder.Default
    private List<MascotaDto> mascotas = new ArrayList<>();
}
