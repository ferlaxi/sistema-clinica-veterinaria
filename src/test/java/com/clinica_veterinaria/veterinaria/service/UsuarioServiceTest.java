package com.clinica_veterinaria.veterinaria.service;

import com.clinica_veterinaria.veterinaria.dto.UsuarioDto;
import com.clinica_veterinaria.veterinaria.entity.Usuario;
import com.clinica_veterinaria.veterinaria.entity.enums.Rol;
import com.clinica_veterinaria.veterinaria.repository.IUsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private IUsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    @DisplayName("Debe obtener un usuario por ID correctamente si existe")
    void obtenerPorId_Exitoso() {
        Long usuarioId = 1L;
        Usuario usuarioSimulado = Usuario.builder()
                .id(usuarioId)
                .username("fernando_vet")
                .password("1234")
                .rol(Rol.ROLE_ADMIN)
                .activo(true)
                .build();

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuarioSimulado));

        UsuarioDto resultado = usuarioService.obtenerPorId(usuarioId);

        assertNotNull(resultado);
        assertEquals("fernando_vet", resultado.getUsername());
        assertEquals(Rol.ROLE_ADMIN, resultado.getRol());

        verify(usuarioRepository, times(1)).findById(usuarioId);
    }

    @Test
    @DisplayName("Debe lanzar una excepción si el usuario no existe")
    void obtenerPorId_NoEncontrado() {
        Long usuarioId = 99L;
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        Exception excepcion = assertThrows(RuntimeException.class, () -> {
            usuarioService.obtenerPorId(usuarioId);
        });

        assertEquals("Usuario no encontrado con ID: " + usuarioId, excepcion.getMessage());
        verify(usuarioRepository, times(1)).findById(usuarioId);
    }
}
