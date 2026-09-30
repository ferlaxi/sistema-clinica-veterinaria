package com.clinica_veterinaria.veterinaria.service;

import com.clinica_veterinaria.veterinaria.dto.UsuarioDto;

import java.util.List;

public interface IUsuarioService {

    List<UsuarioDto> listarTodos();
    UsuarioDto obtenerPorId(Long id);
    UsuarioDto obtenerPorUsername(String username);
    UsuarioDto registrar(UsuarioDto dto);
    UsuarioDto registrarPersonal(UsuarioDto dto);
    UsuarioDto actualizarPersonal(Long id, UsuarioDto dto);
    void eliminar(Long id);
}
