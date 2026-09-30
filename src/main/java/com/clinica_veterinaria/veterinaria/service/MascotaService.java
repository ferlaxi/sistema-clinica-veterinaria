package com.clinica_veterinaria.veterinaria.service;

import com.clinica_veterinaria.veterinaria.dto.MascotaDto;
import com.clinica_veterinaria.veterinaria.entity.Duenio;
import com.clinica_veterinaria.veterinaria.entity.Mascota;
import com.clinica_veterinaria.veterinaria.repository.IDuenioRepository;
import com.clinica_veterinaria.veterinaria.repository.IMascotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MascotaService implements IMascotaService {

    @Autowired
    private IMascotaRepository iMascotaRepository;

    @Autowired
    private IDuenioRepository iDuenioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MascotaDto> listarTodas() {
        return iMascotaRepository.findAll().stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MascotaDto> listarPorDuenio(Long duenioId) {
        return iMascotaRepository.findByDuenioId(duenioId).stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MascotaDto obtenerPorId(Long id) {
        Mascota mascota = iMascotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con ID: " + id));
        return convertirADto(mascota);
    }

    @Override
    @Transactional
    public MascotaDto guardar(MascotaDto dto) {
        Duenio duenio = iDuenioRepository.findById(dto.getDuenioId())
                .orElseThrow(() -> new RuntimeException("Dueño no encontrado con ID: " + dto.getDuenioId()));

        Mascota mascota;
        if (dto.getId() != null) {
            mascota = iMascotaRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Mascota no encontrada con ID: " + dto.getId()));
            mascota.setNombre(dto.getNombre());
            mascota.setEspecie(dto.getEspecie());
            mascota.setRaza(dto.getRaza());
            mascota.setEdad(dto.getEdad());
            mascota.setSexo(dto.getSexo());
            mascota.setObservaciones(dto.getObservaciones());
            mascota.setDuenio(duenio);
        } else {
            mascota = Mascota.builder()
                    .nombre(dto.getNombre())
                    .especie(dto.getEspecie())
                    .raza(dto.getRaza())
                    .edad(dto.getEdad())
                    .sexo(dto.getSexo())
                    .observaciones(dto.getObservaciones())
                    .duenio(duenio)
                    .build();
        }

        return convertirADto(iMascotaRepository.save(mascota));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!iMascotaRepository.existsById(id)) {
            throw new RuntimeException("Mascota no encontrada con ID: " + id);
        }
        iMascotaRepository.deleteById(id);
    }

    private MascotaDto convertirADto(Mascota mascota) {
        return MascotaDto.builder()
                .id(mascota.getId())
                .nombre(mascota.getNombre())
                .especie(mascota.getEspecie())
                .raza(mascota.getRaza())
                .edad(mascota.getEdad())
                .sexo(mascota.getSexo())
                .observaciones(mascota.getObservaciones())
                .duenioId(mascota.getDuenio().getId())
                .nombreDuenio(mascota.getDuenio().getNombre() + " " + mascota.getDuenio().getApellido())
                .build();
    }
}
