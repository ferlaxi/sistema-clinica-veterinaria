package com.clinica_veterinaria.veterinaria.service;

import com.clinica_veterinaria.veterinaria.dto.DuenioDto;
import com.clinica_veterinaria.veterinaria.entity.Duenio;
import com.clinica_veterinaria.veterinaria.entity.Usuario;
import com.clinica_veterinaria.veterinaria.repository.IDuenioRepository;
import com.clinica_veterinaria.veterinaria.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DuenioService implements IDuenioService {

    @Autowired
    private IDuenioRepository duenioRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DuenioDto> listarTodos() {
        return duenioRepository.findAll().stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DuenioDto obtenerPorId(Long id) {
        Duenio duenio = duenioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dueño no encontrado con ID: " + id));
        return convertirADto(duenio);
    }
    @Override
    @Transactional(readOnly = true)
    public DuenioDto obtenerPorUsuarioUsername(String username) {
        return duenioRepository.findByUsuarioUsername(username)
                .map(this::convertirADto)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DuenioDto> buscarPorDni(String dni) {
        return duenioRepository.findByDniContaining(dni).stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DuenioDto guardar(DuenioDto dto) {
        Optional<Duenio> existenteDni = duenioRepository.findByDni(dto.getDni());
        if (existenteDni.isPresent() && !existenteDni.get().getId().equals(dto.getId())) {
            throw new RuntimeException("Ya existe un dueño registrado con el DNI: " + dto.getDni());
        }

        Usuario usuario = null;
        if (dto.getUsuarioId() != null) {
            usuario = usuarioRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.getUsuarioId()));
        }

        Duenio duenio;
        if (dto.getId() != null) {
            duenio = duenioRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Dueño no encontrado con ID: " + dto.getId()));
            duenio.setNombre(dto.getNombre());
            duenio.setApellido(dto.getApellido());
            duenio.setDni(dto.getDni());
            duenio.setTelefono(dto.getTelefono());
            duenio.setDireccion(dto.getDireccion());
            duenio.setUsuario(usuario);
        } else {
            duenio = Duenio.builder()
                    .nombre(dto.getNombre())
                    .apellido(dto.getApellido())
                    .dni(dto.getDni())
                    .telefono(dto.getTelefono())
                    .direccion(dto.getDireccion())
                    .usuario(usuario)
                    .build();
        }

        return convertirADto(duenioRepository.save(duenio));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!duenioRepository.existsById(id)) {
            throw new RuntimeException("Dueño no encontrado con ID: " + id);
        }
        duenioRepository.deleteById(id);
    }

    private DuenioDto convertirADto(Duenio duenio) {
        DuenioDto dto = DuenioDto.builder()
                .id(duenio.getId())
                .nombre(duenio.getNombre())
                .apellido(duenio.getApellido())
                .dni(duenio.getDni())
                .telefono(duenio.getTelefono())
                .direccion(duenio.getDireccion())
                .usuarioId(duenio.getUsuario() != null ? duenio.getUsuario().getId() : null)
                .build();

        if (duenio.getMascotas() != null) {
            dto.setMascotas(duenio.getMascotas().stream().map(m -> {
                com.clinica_veterinaria.veterinaria.dto.MascotaDto mdto = com.clinica_veterinaria.veterinaria.dto.MascotaDto.builder()
                        .id(m.getId())
                        .nombre(m.getNombre())
                        .especie(m.getEspecie())
                        .raza(m.getRaza())
                        .edad(m.getEdad())
                        .build();
                if (m.getTurnos() != null) {
                    mdto.setTurnos(m.getTurnos().stream().map(t -> com.clinica_veterinaria.veterinaria.dto.TurnoDto.builder()
                            .id(t.getId())
                            .fechaHora(t.getFechaHora())
                            .motivo(t.getMotivo())
                            .estado(t.getEstado())
                            .diagnostico(t.getDiagnostico())
                            .build()).collect(java.util.stream.Collectors.toList()));
                }
                return mdto;
            }).collect(java.util.stream.Collectors.toList()));
        }
        return dto;
    }
}