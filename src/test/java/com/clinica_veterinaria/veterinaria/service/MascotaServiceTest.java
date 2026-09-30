package com.clinica_veterinaria.veterinaria.service;

import com.clinica_veterinaria.veterinaria.dto.MascotaDto;
import com.clinica_veterinaria.veterinaria.entity.Duenio;
import com.clinica_veterinaria.veterinaria.entity.Mascota;
import com.clinica_veterinaria.veterinaria.entity.enums.Especie;
import com.clinica_veterinaria.veterinaria.repository.IDuenioRepository;
import com.clinica_veterinaria.veterinaria.repository.IMascotaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MascotaServiceTest {

    @Mock
    private IMascotaRepository mascotaRepository;

    @Mock
    private IDuenioRepository duenioRepository;

    @InjectMocks
    private MascotaService mascotaService;

    @Test
    @DisplayName("Debe guardar la mascota si el Dueño asignado existe")
    void guardar_Exitoso() {
        Long duenioId = 1L;
        MascotaDto requestDto = MascotaDto.builder()
                .nombre("Michi")
                .especie(Especie.GATO)
                .duenioId(duenioId)
                .build();

        Duenio duenioSimulado = new Duenio();
        duenioSimulado.setId(duenioId);
        duenioSimulado.setNombre("Carlos");
        duenioSimulado.setApellido("Gómez");

        Mascota mascotaGuardada = Mascota.builder()
                .id(100L)
                .nombre("Michi")
                .especie(Especie.GATO)
                .duenio(duenioSimulado)
                .build();

        when(duenioRepository.findById(duenioId)).thenReturn(Optional.of(duenioSimulado));
        when(mascotaRepository.save(any(Mascota.class))).thenReturn(mascotaGuardada);

        MascotaDto resultado = mascotaService.guardar(requestDto);

        assertNotNull(resultado);
        assertEquals(Especie.GATO, resultado.getEspecie());
        assertEquals("Michi", resultado.getNombre());
        assertEquals("Carlos Gómez", resultado.getNombreDuenio());

        verify(mascotaRepository, times(1)).save(any(Mascota.class));
    }

    @Test
    @DisplayName("Debe fallar al guardar mascota si el Dueño asignado NO existe")
    void guardar_DuenioNoExiste_Falla() {
        Long duenioFalso = 99L;
        MascotaDto requestDto = MascotaDto.builder()
                .nombre("Rex")
                .duenioId(duenioFalso)
                .build();

        when(duenioRepository.findById(duenioFalso)).thenReturn(Optional.empty());

        Exception e = assertThrows(RuntimeException.class, () -> {
            mascotaService.guardar(requestDto);
        });

        assertEquals("Dueño no encontrado con ID: " + duenioFalso, e.getMessage());
        verify(mascotaRepository, never()).save(any(Mascota.class));
    }
}