package com.clinica_veterinaria.veterinaria.dto;

import com.clinica_veterinaria.veterinaria.entity.enums.Especie;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;
import java.util.ArrayList;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MascotaDto {

    private Long id;
    @NotBlank(message = "El nombre de la mascota no puede estar vacío")
    private String nombre;

    @NotNull(message = "La especie no puede ser nula")
    private Especie especie;

    @NotBlank(message = "La raza no puede estar vacía")
    private String raza;

    private Integer edad;
    private String sexo;
    private String observaciones;

    @NotNull(message = "El id del dueño no puede ser nulo")
    private Long duenioId;

    private String nombreDuenio;

    @Builder.Default
    private List<TurnoDto> turnos = new ArrayList<>();
}
