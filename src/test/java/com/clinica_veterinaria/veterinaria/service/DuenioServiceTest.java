package com.clinica_veterinaria.veterinaria.service;

import com.clinica_veterinaria.veterinaria.dto.DuenioDto;
import com.clinica_veterinaria.veterinaria.entity.Duenio;
import com.clinica_veterinaria.veterinaria.repository.IDuenioRepository;
import com.clinica_veterinaria.veterinaria.repository.IUsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DuenioServiceTest {

    @Mock
    private IDuenioRepository duenioRepository;

    @Mock
    private IUsuarioRepository usuarioRepository;

    @InjectMocks
    private DuenioService duenioService;

    @Test
    @DisplayName("Debe guardar un Dueño exitosamente si el DNI no está duplicado")
    void guardar_Exitoso() {
        DuenioDto requestDto = DuenioDto.builder()
                .nombre("Juan")
                .apellido("Pérez")
                .dni("12345678")
                .telefono("555-1234")
                .build();

        Duenio duenioGuardado = Duenio.builder()
                .id(1L)
                .nombre("Juan")
                .apellido("Pérez")
                .dni("12345678")
                .build();

        when(duenioRepository.existsByDni("12345678")).thenReturn(false);
        when(duenioRepository.save(any(Duenio.class))).thenReturn(duenioGuardado);

        DuenioDto resultado = duenioService.guardar(requestDto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Juan", resultado.getNombre());

        verify(duenioRepository, times(1)).save(any(Duenio.class));
    }

    @Test
    @DisplayName("Debe fallar al crear Dueño si el DNI ya existe")
    void guardar_DniDuplicado_Falla() {
        DuenioDto requestDto = DuenioDto.builder()
                .nombre("Ana")
                .dni("87654321")
                .build();

        when(duenioRepository.existsByDni("87654321")).thenReturn(true);

        Exception e = assertThrows(RuntimeException.class, () -> {
            duenioService.guardar(requestDto);
        });

        assertEquals("Ya existe un dueño registrado con el DNI: " + requestDto.getDni(), e.getMessage());
        verify(duenioRepository, never()).save(any(Duenio.class));
    }

    @Test
    @DisplayName("Debe eliminar un Dueño si existe")
    void eliminar_Exito() {
        Long idDuenio = 5L;
        when(duenioRepository.existsById(idDuenio)).thenReturn(true);

        duenioService.eliminar(idDuenio);

        verify(duenioRepository, times(1)).deleteById(idDuenio);
    }
}