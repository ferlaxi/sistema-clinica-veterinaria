package com.clinica_veterinaria.veterinaria.service;

import com.clinica_veterinaria.veterinaria.dto.TurnoDto;
import com.clinica_veterinaria.veterinaria.entity.enums.EstadoTurno;

import java.util.List;

public interface ITurnoService {
    List<TurnoDto> listarTodos();
    List<TurnoDto> listarPorMascota(Long mascotaId);
    List<TurnoDto> listarPorDuenio(Long duenioId);
    List<TurnoDto> listarPorVeterinario(Long veterinarioId);
    TurnoDto obtenerPorId(Long id);
    TurnoDto registrarTurno(TurnoDto dto);
    TurnoDto cambiarEstado(Long turnoId, EstadoTurno nuevoEstado);
    TurnoDto actualizarDiagnostico(Long turnoId, String diagnostico);
}