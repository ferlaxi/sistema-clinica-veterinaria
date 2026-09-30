package com.clinica_veterinaria.veterinaria.dto;

import com.clinica_veterinaria.veterinaria.entity.enums.EstadoTurno;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TurnoDto {

    private Long id;
    private LocalDateTime fechaHora;

    @NotBlank(message = "El motivo no puede estar vacío")
    private String motivo;
    private EstadoTurno estado;
    private String diagnostico;

    @NotNull(message = "El id de la mascota no puede ser nulo")
    private Long mascotaId;
    private String nombreMascota;
    private Long veterinarioId;
    private String nombreVeterinario;

    private String nombreDuenio;
    private String telefonoDuenio;
}
