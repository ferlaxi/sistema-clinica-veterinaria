package com.clinica_veterinaria.veterinaria.service;

import com.clinica_veterinaria.veterinaria.dto.DuenioDto;

import java.util.List;

public interface IDuenioService {
    List<DuenioDto> listarTodos();
    DuenioDto obtenerPorId(Long id);
    DuenioDto obtenerPorUsuarioUsername(String username);
    List<DuenioDto> buscarPorDni(String dni);
    DuenioDto guardar(DuenioDto dto);
    void eliminar(Long id);
}