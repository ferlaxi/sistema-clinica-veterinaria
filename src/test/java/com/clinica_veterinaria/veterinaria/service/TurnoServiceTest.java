package com.clinica_veterinaria.veterinaria.service;

import com.clinica_veterinaria.veterinaria.dto.TurnoDto;
import com.clinica_veterinaria.veterinaria.entity.Mascota;
import com.clinica_veterinaria.veterinaria.entity.Turno;
import com.clinica_veterinaria.veterinaria.entity.enums.EstadoTurno;
import com.clinica_veterinaria.veterinaria.repository.IMascotaRepository;
import com.clinica_veterinaria.veterinaria.repository.ITurnoRepository;
import com.clinica_veterinaria.veterinaria.repository.IUsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TurnoServiceTest {

    @Mock
    private ITurnoRepository turnoRepository;

    @Mock
    private IMascotaRepository mascotaRepository;

    @Mock
    private IUsuarioRepository usuarioRepository;

    @InjectMocks
    private TurnoService turnoService;

    @Test
    @DisplayName("Debe registrar un turno exitosamente asignando Estado default = SOLICITADO")
    void registrarTurno_Exitoso() {
        Long mascotaId = 10L;
        TurnoDto turnoDtoRespuesta = TurnoDto.builder()
                .mascotaId(mascotaId)
                .motivo("Vacunación anual")
                .build();

        Mascota mascotaSimulada = new Mascota();
        mascotaSimulada.setId(mascotaId);
        mascotaSimulada.setNombre("Firulais");

        Turno turnoGuardado = Turno.builder()
                .id(1L)
                .fechaHora(LocalDateTime.now())
                .motivo("Vacunación anual")
                .estado(EstadoTurno.SOLICITADO)
                .mascota(mascotaSimulada)
                .build();

        when(mascotaRepository.findById(mascotaId)).thenReturn(Optional.of(mascotaSimulada));
        when(turnoRepository.save(any(Turno.class))).thenReturn(turnoGuardado);

        TurnoDto resultado = turnoService.registrarTurno(turnoDtoRespuesta);

        assertNotNull(resultado);
        assertEquals("Vacunación anual", resultado.getMotivo());
        assertEquals(EstadoTurno.SOLICITADO, resultado.getEstado());
        assertEquals("Firulais", resultado.getNombreMascota());

        verify(mascotaRepository, times(1)).findById(mascotaId);
        verify(turnoRepository, times(1)).save(any(Turno.class));
    }

    @Test
    @DisplayName("Debe fallar al registrar turno si la mascota enviada no existe en la Base")
    void registrarTurno_FallaMascotaInexistente() {
        Long mascotaIncorrecta = 99L;
        TurnoDto turnoDtoRequest = TurnoDto.builder()
                .mascotaId(mascotaIncorrecta)
                .motivo("Revisión de oído")
                .build();

        when(mascotaRepository.findById(mascotaIncorrecta)).thenReturn(Optional.empty());

        Exception excepcion = assertThrows(RuntimeException.class, () -> {
            turnoService.registrarTurno(turnoDtoRequest);
        });

        assertEquals("Mascota no encontrada con ID: " + mascotaIncorrecta, excepcion.getMessage());

        verify(turnoRepository, never()).save(any(Turno.class));
    }
}
