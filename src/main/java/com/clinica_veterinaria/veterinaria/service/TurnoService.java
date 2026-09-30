package com.clinica_veterinaria.veterinaria.service;

import com.clinica_veterinaria.veterinaria.dto.TurnoDto;
import com.clinica_veterinaria.veterinaria.entity.Mascota;
import com.clinica_veterinaria.veterinaria.entity.Turno;
import com.clinica_veterinaria.veterinaria.entity.Duenio;
import com.clinica_veterinaria.veterinaria.entity.Usuario;
import com.clinica_veterinaria.veterinaria.entity.enums.EstadoTurno;
import com.clinica_veterinaria.veterinaria.repository.IMascotaRepository;
import com.clinica_veterinaria.veterinaria.repository.ITurnoRepository;
import com.clinica_veterinaria.veterinaria.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TurnoService implements ITurnoService {

    @Autowired
    private ITurnoRepository iTurnoRepository;

    @Autowired
    private IMascotaRepository iMascotaRepository;

    @Autowired
    private IUsuarioRepository iUsuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TurnoDto> listarTodos() {
        return iTurnoRepository.findAll().stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurnoDto> listarPorMascota(Long mascotaId) {
        return iTurnoRepository.findByMascotaId(mascotaId).stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurnoDto> listarPorDuenio(Long duenioId) {
        return iTurnoRepository.findByMascotaDuenioId(duenioId).stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurnoDto> listarPorVeterinario(Long veterinarioId) {
        return iTurnoRepository.findByVeterinarioId(veterinarioId).stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TurnoDto obtenerPorId(Long id) {
        Turno turno = iTurnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado con ID: " + id));
        return convertirADto(turno);
    }

    @Override
    @Transactional
    public TurnoDto registrarTurno(TurnoDto dto) {
        Mascota mascota = iMascotaRepository.findById(dto.getMascotaId())
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con ID: " + dto.getMascotaId()));

        Usuario veterinario = null;
        if (dto.getVeterinarioId() != null) {
            veterinario = iUsuarioRepository.findById(dto.getVeterinarioId())
                    .orElseThrow(() -> new RuntimeException("Veterinario no encontrado con ID: " + dto.getVeterinarioId()));
        }

        Turno turno = Turno.builder()
                .fechaHora(dto.getFechaHora() != null ? dto.getFechaHora() : LocalDateTime.now())
                .motivo(dto.getMotivo())
                .estado(dto.getEstado() != null ? dto.getEstado() : EstadoTurno.SOLICITADO)
                .diagnostico(dto.getDiagnostico())
                .mascota(mascota)
                .veterinario(veterinario)
                .build();

        return convertirADto(iTurnoRepository.save(turno));
    }

    @Override
    @Transactional
    public TurnoDto cambiarEstado(Long turnoId, EstadoTurno nuevoEstado) {
        Turno turno = iTurnoRepository.findById(turnoId)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado con ID: " + turnoId));
        turno.setEstado(nuevoEstado);
        return convertirADto(iTurnoRepository.save(turno));
    }

    @Override
    @Transactional
    public TurnoDto actualizarDiagnostico(Long turnoId, String diagnostico) {
        Turno turno = iTurnoRepository.findById(turnoId)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado con ID: " + turnoId));
        turno.setDiagnostico(diagnostico);
        return convertirADto(iTurnoRepository.save(turno));
    }

    private TurnoDto convertirADto(Turno turno) {
        Duenio duenio = turno.getMascota().getDuenio();
        return TurnoDto.builder()
                .id(turno.getId())
                .fechaHora(turno.getFechaHora())
                .motivo(turno.getMotivo())
                .estado(turno.getEstado())
                .diagnostico(turno.getDiagnostico())
                .mascotaId(turno.getMascota().getId())
                .nombreMascota(turno.getMascota().getNombre())
                .veterinarioId(turno.getVeterinario() != null ? turno.getVeterinario().getId() : null)
                .nombreVeterinario(turno.getVeterinario() != null ? turno.getVeterinario().getUsername() : null)
                .nombreDuenio(duenio != null ? duenio.getNombre() + " " + duenio.getApellido() : "Sin dueño")
                .telefonoDuenio(duenio != null ? duenio.getTelefono() : "Sin teléfono")
                .build();
    }
}
