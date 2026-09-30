package com.clinica_veterinaria.veterinaria.service;

import com.clinica_veterinaria.veterinaria.dto.MascotaDto;

import java.util.List;

public interface IMascotaService {
    List<MascotaDto> listarTodas();
    List<MascotaDto> listarPorDuenio(Long duenioId);
    MascotaDto obtenerPorId(Long id);
    MascotaDto guardar(MascotaDto dto);
    void eliminar(Long id);
}
